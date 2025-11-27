package com.example.myapplication.viewmodels

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class AuthViewModelTest {

    private lateinit var context: Context
    private lateinit var prefs: SharedPreferences
    private lateinit var prefsEditor: SharedPreferences.Editor
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        // Mock context y shared preferences
        context = mock()
        prefs = mock()
        prefsEditor = mock()

        whenever(context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE))
            .thenReturn(prefs)

        whenever(prefs.edit()).thenReturn(prefsEditor)
        whenever(prefsEditor.putString(any(), any())).thenReturn(prefsEditor)

        // Inicializar ViewModel con contexto mockeado
        viewModel = AuthViewModel(context)
    }

    @Test
    fun `login correcto debe asignar currentUser y isLoggedIn`() {
        val userList = listOf(User("Juan", "Perez", "jperez", "1234"))
        val json = Gson().toJson(userList)

        whenever(prefs.getString("users", "[]")).thenReturn(json)

        val result = viewModel.login("jperez", "1234")

        assert(result)
        assert(viewModel.isLoggedIn.value)
        assert(viewModel.currentUser.value?.username == "jperez")
    }

    @Test
    fun `login incorrecto no debe autenticar`() {
        whenever(prefs.getString("users", "[]")).thenReturn("[]")

        val result = viewModel.login("jperez", "wrongpass")

        assert(!result)
        assert(!viewModel.isLoggedIn.value)
        assert(viewModel.currentUser.value == null)
    }

    @Test
    fun `register debe guardar un usuario nuevo`() {
        val emptyListJson = "[]"
        whenever(prefs.getString("users", "[]")).thenReturn(emptyListJson)

        val newUser = User("Juan", "Perez", "jperez", "1234")

        val result = viewModel.register(newUser)

        assert(result)
        verify(prefsEditor).putString(eq("users"), any())
        verify(prefsEditor).apply()
    }

    @Test
    fun `register no debe permitir username duplicado`() {
        val existing = listOf(User("Juan", "Perez", "jperez", "1234"))
        val json = Gson().toJson(existing)

        whenever(prefs.getString("users", "[]")).thenReturn(json)

        val duplicateUser = User("Juan", "Perez", "jperez", "aaaa")

        val result = viewModel.register(duplicateUser)

        assert(!result)
        verify(prefsEditor, never()).putString(any(), any())
    }

    @Test
    fun `logout debe limpiar estado`() {
        // Simula usuario logueado
        viewModel.isLoggedIn.value = true
        viewModel.currentUser.value = User("Juan", "Perez", "jperez", "1234")

        viewModel.logout()

        assert(!viewModel.isLoggedIn.value)
        assert(viewModel.currentUser.value == null)
    }
}
