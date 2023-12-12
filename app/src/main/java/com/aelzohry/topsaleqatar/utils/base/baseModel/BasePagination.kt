package com.aelzohry.topsaleqatar.utils.base.baseModel

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
data class BasePagination<T>(val data: List<T>, val pagination: Pagination) : BaseModel() {

    data class Pagination(val total: Int, val perPage: Int, val lastPage: Int, val currentPage: Int)

}