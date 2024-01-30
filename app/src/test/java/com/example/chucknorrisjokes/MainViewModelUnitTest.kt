package com.example.chucknorrisjokes

import com.example.chucknorrisjokes.core.UiObserver
import com.example.chucknorrisjokes.presentation.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.example.chucknorrisjokes.presentation.MainActivity
import com.example.chucknorrisjokes.core.RunAsync
import com.example.chucknorrisjokes.presentation.UiStateObservable
import com.example.chucknorrisjokes.presentation.UiStateObserver
import com.example.chucknorrisjokes.domain.LoadResult
import com.example.chucknorrisjokes.data.Repository

class MainViewModelUnitTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var runAsync: FakeRunAsync
    private lateinit var uiStateObservable: FakeObservable
    private lateinit var repository: FakeRepository
    private lateinit var order: Order

    @Before
    fun setUp() {
        order = Order()
        runAsync = FakeRunAsync(order)
        uiStateObservable = FakeObservable(order)
        repository = FakeRepository(order, true)
        mainViewModel = MainViewModel.Base(runAsync, uiStateObservable)
    }

    @Test
    fun successResponseTwice() {
        mainViewModel.init(true)
        uiStateObservable.checkUiState(UiState.Initial())
        order.check(OBSERVABLE_UPDATE)

        mainViewModel.startGettingUpdates(object : UiStateObserver {
            override fun update(data: UiState) = Unit
        })
        order.check(OBSERVABLE_UPDATE, OBSERVABLE_UPDATE_OBSERVER)

        //user click "Get Joke!"
        mainViewModel.loadJoke()
        uiStateObservable.checkUiState(UiState.Loading)
        order.check(
            OBSERVABLE_UPDATE, OBSERVABLE_UPDATE_OBSERVER,
            OBSERVABLE_UPDATE,
            RUN_ASYNC_BACKGROUND,
            REPOSITORY_JOKE
        )

        runAsync.pingResult()
        uiStateObservable.checkUiState(UiState.Initial("Chuck Norris is the world's most popular loner."))
        order.check(
            OBSERVABLE_UPDATE, OBSERVABLE_UPDATE_OBSERVER, OBSERVABLE_UPDATE, RUN_ASYNC_BACKGROUND, REPOSITORY_JOKE,
            RUN_ASYNC_UI,
            OBSERVABLE_UPDATE
        )

        //user click "Get Joke!" second time
        mainViewModel.loadJoke()
        uiStateObservable.checkUiState(UiState.Loading)
        order.check(
            OBSERVABLE_UPDATE, OBSERVABLE_UPDATE_OBSERVER, OBSERVABLE_UPDATE, RUN_ASYNC_BACKGROUND, REPOSITORY_JOKE, RUN_ASYNC_UI, OBSERVABLE_UPDATE,
            OBSERVABLE_UPDATE,
            RUN_ASYNC_BACKGROUND,
            REPOSITORY_JOKE
        )

        runAsync.pingResult()
        uiStateObservable.checkUiState(UiState.Initial("The signs outside of Chuck Norris' properties all say \\\"TRESPASSERS WILL BE NORRISED\\\""))
        order.check(
            OBSERVABLE_UPDATE, OBSERVABLE_UPDATE_OBSERVER, OBSERVABLE_UPDATE, RUN_ASYNC_BACKGROUND, REPOSITORY_JOKE, RUN_ASYNC_UI, OBSERVABLE_UPDATE, OBSERVABLE_UPDATE, RUN_ASYNC_BACKGROUND, REPOSITORY_JOKE,
            RUN_ASYNC_UI,
            OBSERVABLE_UPDATE
        )

    }

    @Test
    fun successResponseAfterFailed(){
        mainViewModel.init(true)
        uiStateObservable.checkUiState(UiState.Initial())
        order.check(OBSERVABLE_UPDATE)

        mainViewModel.startGettingUpdates(object : UiStateObserver {
            override fun update(data: UiState) = Unit
        })
        order.check(OBSERVABLE_UPDATE, OBSERVABLE_UPDATE_OBSERVER)

        //user click "Get Joke!:"
        mainViewModel.loadJoke()
        uiStateObservable.checkUiState(UiState.Loading)
        order.check(
            OBSERVABLE_UPDATE, OBSERVABLE_UPDATE_OBSERVER,
            OBSERVABLE_UPDATE,
            RUN_ASYNC_BACKGROUND,
            REPOSITORY_JOKE
        )

        runAsync.pingResult()
        uiStateObservable.checkUiState(UiState.Initial("Chuck Norris is the world's most popular loner."))
        order.check(
            OBSERVABLE_UPDATE, OBSERVABLE_UPDATE_OBSERVER, OBSERVABLE_UPDATE, RUN_ASYNC_BACKGROUND, REPOSITORY_JOKE,
            RUN_ASYNC_UI,
            OBSERVABLE_UPDATE
        )

        repository.isSuccessResponse(false)

        //user click "Get Joke!" second time"
        mainViewModel.loadJoke()
        uiStateObservable.checkUiState(UiState.Loading)
        order.check(
            OBSERVABLE_UPDATE, OBSERVABLE_UPDATE_OBSERVER, OBSERVABLE_UPDATE, RUN_ASYNC_BACKGROUND, REPOSITORY_JOKE, RUN_ASYNC_UI, OBSERVABLE_UPDATE,
            OBSERVABLE_UPDATE,
            RUN_ASYNC_BACKGROUND,
            REPOSITORY_JOKE
        )

        runAsync.pingResult()
        uiStateObservable.checkUiState(UiState.Error("Server unavailable. Please try later."))
        order.check(
            OBSERVABLE_UPDATE, OBSERVABLE_UPDATE_OBSERVER, OBSERVABLE_UPDATE, RUN_ASYNC_BACKGROUND, REPOSITORY_JOKE, RUN_ASYNC_UI, OBSERVABLE_UPDATE, OBSERVABLE_UPDATE, RUN_ASYNC_BACKGROUND, REPOSITORY_JOKE,
            RUN_ASYNC_UI,
            OBSERVABLE_UPDATE
        )
    }

    companion object {
        private const val RUN_ASYNC_BACKGROUND = "RunAsync#runAsync{backgroundBlock.invoke()}"
        private const val RUN_ASYNC_UI = "RunAsync#runAsync{uiBlock.invoke(result)}"
        private const val RUN_ASYNC_CLEAR = "RunAsync#clear"
        private const val OBSERVABLE_UPDATE = "UiStateObservable#update"
        private const val OBSERVABLE_UPDATE_OBSERVER = "UiStateObservable#updateObserver"
        private const val OBSERVABLE_CLEAR = "UiStateObservable#clear"
        private const val REPOSITORY_JOKE = "Repository#joke"
    }

    @Suppress("UNCHECKED_CAST")
    private class FakeRunAsync(private val order: Order) : RunAsync {

        private var cachedResult: Any = Any()
        private var cachedUiBlock: (Any) -> Unit = {}

        override fun <T : Any> runAsync(
            coroutineScope: CoroutineScope,
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) = runBlocking {
            order.add(RUN_ASYNC_BACKGROUND)
            cachedResult = backgroundBlock.invoke()
            cachedUiBlock = uiBlock as (Any) -> Unit
        }

        fun pingResult() {
            order.add(RUN_ASYNC_UI)
            cachedUiBlock.invoke(cachedResult)
        }

        private var clearCount = 0
        override fun clear() {
            clearCount++
            order.add(RUN_ASYNC_CLEAR)
        }

        fun checkClearCalledCount(times: Int) {
            assertEquals(times, clearCount)
        }
    }

    private class FakeObservable(private val order: Order) : UiStateObservable {

        private var cachedUiState: UiState = UiState.Empty
        private var cachedUiObservable: UiObserver<UiState> = object : UiStateObserver {
            override fun update(data: UiState) = Unit
        }

        override fun update(data: UiState) {
            cachedUiState = data
            order.add(OBSERVABLE_UPDATE)
        }

        fun checkUiState(uiState: UiState) = assertEquals(uiState, cachedUiState)

        override fun updateObserver(observer: UiObserver<UiState>) {
            cachedUiObservable = observer
            order.add(OBSERVABLE_UPDATE_OBSERVER)
        }

        override fun clear() {
            cachedUiState = UiState.Empty
            order.add(OBSERVABLE_CLEAR)
        }
    }

    private class FakeRepository(
        private val order: Order,
        private var isSuccessResponse: Boolean
    ) : Repository {

        private val jokes = listOf(
            "Chuck Norris is the world's most popular loner.",
            "The signs outside of Chuck Norris' properties all say \"TRESPASSERS WILL BE NORRISED\""
        )
        private var index = 0

        override suspend fun joke(): LoadResult {
            order.add(REPOSITORY_JOKE)
            return if (isSuccessResponse) {
                LoadResult.Success(joke = jokes[(index++) % jokes.size])
            } else {
                LoasResult.Error(message = "Server unavailable. Please try later.")
            }
        }

        fun isSuccessResponse(boolean: Boolean) {
            isSuccessResponse = boolean
        }
    }
}