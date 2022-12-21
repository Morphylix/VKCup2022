package com.morphylix.android.dzentest.data.cache

import com.morphylix.android.dzentest.R
import com.morphylix.android.dzentest.domain.model.Category

/*
I've decided not to use Room to simplify my solution.
However in a real project it would be better to use DB instead of in-memory cache obviously.
*/
object InMemoryCategoryList {
    val categoryList = listOf(
        Category("Рецепты", R.drawable.recipes),
        Category("Работа", R.drawable.work),
        Category("Юмор", R.drawable.humor),
        Category("Прогулки", R.drawable.walking),
        Category("Спорт", R.drawable.sport),
        Category("Отдых", R.drawable.rest),
        Category("Автомобили", R.drawable.cars),
        Category("Кино", R.drawable.cinema),
        Category("Сериалы", R.drawable.serials),
        Category("Новости", R.drawable.news),
        Category("Рестораны", R.drawable.restaurants),
        Category("Политика", R.drawable.politics),
        Category("Еда", R.drawable.food)
    )
}