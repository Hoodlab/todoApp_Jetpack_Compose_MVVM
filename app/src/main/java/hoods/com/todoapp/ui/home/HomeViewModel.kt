package hoods.com.todoapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hoods.com.todoapp.Graph
import hoods.com.todoapp.data.Todo
import hoods.com.todoapp.data.TodoDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


class HomeViewModel(private val todoDataSource: TodoDataSource = Graph.todoRepo) : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState>
        get() = _state

    val todoList = todoDataSource.selectAll
    val selected = MutableStateFlow(_state.value.selected)

    init {
        viewModelScope.launch {
            combine(todoList, selected) { todoList: List<Todo>, selected: Boolean ->
                HomeViewState(todoList, selected)
            }.collect {
                _state.value = it
            }
        }
    }

    fun updateTodo(selected: Boolean, id: Long) = viewModelScope.launch {
        todoDataSource.updateTodo(selected, id)
    }

    fun delete(todo: Todo) = viewModelScope.launch {
        todoDataSource.deleteTodo(todo)
    }


}

data class HomeViewState(
    val todoList: List<Todo> = emptyList(),
    val selected: Boolean = false,
)

