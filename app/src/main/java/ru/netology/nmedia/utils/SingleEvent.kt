package ru.netology.nmedia.utils

import androidx.annotation.Nullable

/**
 * Вероятно, этот клас-обёртка имеет недостатки, которые нивелируются, если вместо него использовать
 * SingleLiveEvent. Например, мне показалось что его использование с большей вероятностью
 * может привести к непреднамеренным ошибкам в коде.
 *
 * Тем не менее, я нашёл интересным
 * попытаться реальзовать ДЗ с его помощью после прочтения [статьи]
 * (#facepalm: не смог здесь заинлайнить ссылку),
 * в которой, впрочем, указано, что она уже неактуальна. А с актуальной информацией
 * по предложенной в этой статье ссылке я не справился.
 *
 *
 *
 * @see <a href="https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-
 * and-other-events-the-singleliveevent-case-ac2622673150">Та самая статья </a>
 */

class SingleEvent<T>(private val content: T? = null) {
    private var hasBeenHandled = false

    /**
     * Calls the specified function [block] and returns its result
     * if event has not been handled
     *
     * Returns null if this event has been handled.
     */
    //показалось, что этот метод может быть полезен для удобной обработки Unit внутри его лямбды
    @Nullable
    fun <R> runIfNotHandled(block: () -> R): R? {
        val result: R?
        if (hasBeenHandled) result = null
        else {
            result = block()
            hasBeenHandled = true
        }
        return result
    }

    /**
     * Returns [content] if it has not been handled
     *
     * Returns null if this [content] is null or has been handled.
     */
    @Nullable
    fun getContentIfNotHandled(): T? =
        if (hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
}