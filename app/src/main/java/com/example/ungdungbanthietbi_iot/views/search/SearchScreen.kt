package com.example.ungdungbanthietbi_iot.views.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ungdungbanthietbi_iot.viewModels.DeviceViewModel
import com.example.ungdungbanthietbi_iot.navigation.Screen
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Tạo DataStore với tên "search_history_datastore"
val Context.searchHistoryDataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history_datastore")

class SearchHistoryManager(context: Context) {
    private val dataStore = context.searchHistoryDataStore
    private val gson = Gson()

    companion object {
        private val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")
    }

    // Lưu lịch sử tìm kiếm
    suspend fun saveSearchQuery(query: String) {
        if (query.isBlank()) return // Không lưu nếu query rỗng

        // Lấy danh sách lịch sử hiện tại
        val currentHistory = getSearchHistoryList()
        if (currentHistory.contains(query)) return // Không lưu nếu đã tồn tại
        // Xóa query cũ nếu đã tồn tại để tránh trùng lặp
        val updatedHistory = currentHistory.toMutableList()
        updatedHistory.remove(query)
        // Thêm query mới vào đầu danh sách
        updatedHistory.add(0, query)

        // Giới hạn số lượng lịch sử (ví dụ: tối đa 10 mục)
        val maxHistorySize = 10
        val limitedHistory = updatedHistory.take(maxHistorySize)

        // Lưu danh sách mới vào DataStore
        dataStore.edit { preferences ->
            val json = gson.toJson(limitedHistory)
            preferences[SEARCH_HISTORY_KEY] = json
        }
    }

    // Lấy lịch sử tìm kiếm dưới dạng Flow
    fun getSearchHistory(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            val json = preferences[SEARCH_HISTORY_KEY]
            if (json != null) {
                val type = object : TypeToken<List<String>>() {}.type
                gson.fromJson(json, type)
            } else {
                emptyList()
            }
        }
    }

    // Lấy lịch sử tìm kiếm dưới dạng danh sách (không phải Flow)
    private suspend fun getSearchHistoryList(): List<String> {
        val json = dataStore.data.map { preferences ->
            preferences[SEARCH_HISTORY_KEY] ?: ""
        }.firstOrNull() ?: ""
        return if (json.isNotEmpty()) {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    // Xóa một mục cụ thể trong lịch sử tìm kiếm
    suspend fun removeSearchQuery(query: String) {
        val currentHistory = getSearchHistoryList()
        val updatedHistory = currentHistory.toMutableList()
        updatedHistory.remove(query)

        // Lưu danh sách mới vào DataStore
        dataStore.edit { preferences ->
            if (updatedHistory.isEmpty()) {
                preferences.remove(SEARCH_HISTORY_KEY)
            } else {
                val json = gson.toJson(updatedHistory)
                preferences[SEARCH_HISTORY_KEY] = json
            }
        }
    }

    // Xóa toàn bộ lịch sử tìm kiếm
    suspend fun clearSearchHistory() {
        dataStore.edit { preferences ->
            preferences.remove(SEARCH_HISTORY_KEY)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    username: String?,
    idCustomer: String?,
    token: String?
) {
    val deviceViewModel: DeviceViewModel = viewModel()
    // Lấy từ khóa tìm kiếm từ ViewModel
    val searchQuery by deviceViewModel.searchQuery.collectAsState()

    // FocusRequester để tự động focus ô tìm kiếm
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Khởi tạo SearchHistoryManager với Context
    val context = LocalContext.current
    val searchHistoryManager = remember { SearchHistoryManager(context) }
    // Lấy lịch sử tìm kiếm từ DataStore dưới dạng Flow và chuyển thành State
    val searchHistory by searchHistoryManager.getSearchHistory().collectAsState(initial = emptyList())

    // Coroutine scope để gọi các hàm suspend
    val coroutineScope = rememberCoroutineScope()

    // Tự động focus ô tìm kiếm và hiển thị bàn phím khi màn hình được tải
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
                        // Ô nhập liệu tìm kiếm
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { newQuery ->
                                deviceViewModel.updateSearchQuery(newQuery)
                            },
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 5.dp)
                                .focusRequester(focusRequester), // Gắn FocusRequester
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                cursorColor = Color(0xFF5D9EFF)
                            ),
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { deviceViewModel.updateSearchQuery("") }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .background(Color.Gray, shape = CircleShape)
                                                .padding(3.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Close,
                                                contentDescription = "Clear",
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            },
                            placeholder = { Text(text = "Tìm kiếm ...", color = Color.LightGray) },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                textAlign = TextAlign.Start
                            ),
                            shape = RoundedCornerShape(25.dp),
                            singleLine = true
                        )

                },
                actions = {
                    IconButton(onClick = {
                        if (searchQuery.isNotEmpty()) {
                            // Lưu từ khóa tìm kiếm vào DataStore
                            coroutineScope.launch {
                                searchHistoryManager.saveSearchQuery(searchQuery)
                                //deviceViewModel.searchDevice(searchQuery)
                            }

                            // Điều hướng sang SearchResultsScreen
                            if (username != null) {
                                navController.navigate(
                                    Screen.Search_Results.route + "?query=${searchQuery}&username=${username}&idCustomer=$idCustomer&token=$token"
                                )
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5D9EFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // Màu nền xám nhạt
        ) {
            // Hiển thị lịch sử tìm kiếm
            if (searchHistory.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LỊCH SỬ TÌM KIẾM",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    )
                    // Nút xóa toàn bộ lịch sử dạng văn bản
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                searchHistoryManager.clearSearchHistory()
                            }
                        }
                    ) {
                        Text(
                            text = "XÓA LỊCH SỬ TÌM KIẾM",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(searchHistory) { historyItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Cập nhật searchQuery trong ViewModel
                                    deviceViewModel.updateSearchQuery(historyItem)
                                    deviceViewModel.searchDevice(historyItem)
                                    // Điều hướng sang SearchResultsScreen
                                    if (username != null) {
                                        navController.navigate(
                                            Screen.Search_Results.route + "?query=${historyItem}&username=${username}&idCustomer=$idCustomer&token=$token"
                                        )
                                    } else {
                                        navController.navigate(
                                            Screen.Search_Results.route + "?query=${historyItem}"
                                        )
                                    }
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.History,
                                    contentDescription = "History",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = historyItem,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                )
                            }
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        searchHistoryManager.removeSearchQuery(historyItem)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Xóa lịch sử",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Gray
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

