package utils

data class TorrentInfo (
    var name: String,
    var sizeKb: Long,
    var seeds: Long,
    var peers: Long,
    var status: Status,
    var comments: Long,
    var date: Long
) {
    fun getParameterBySortBy(sortBy: SortBy): Long = when (sortBy) {
        SortBy.SIZE -> sizeKb
        SortBy.SEED -> seeds
        SortBy.PEER -> peers
        SortBy.COMM -> comments
        SortBy.DATE -> date
        else -> 0
    }
}

enum class SortBy {
    SIZE, SEED, PEER, COMM, DATE, NONE
}

enum class SortType {
    ASC, DESC, NONE
}

enum class FilterBy {
    SIZE_LESS_13,
    SIZE_13_22,
    SIZE_22_40,
    SIZE_40_95,
    SIZE_MORE_95,
    STATUS_GOLD,
    STATUS_SILVER,
    DATE_TODAY,
    DATE_YESTERDAY,
    DATE_LAST_3_DAYS,
    DATE_LAST_WEEK,
    DATE_LAST_MONTH,
    NONE
}

enum class Status {
    GOLD, SILVER, NONE
}

class TorrentUtils {
    companion object {
        fun getSelectIndexBySortBy(sortBy: SortBy): Int = when (sortBy) {
            SortBy.SEED -> 1
            SortBy.PEER -> 2
            SortBy.SIZE -> 3
            SortBy.COMM -> 4
            else -> 0
        }

        fun getSelectIndexBySortType(sortType: SortType): Int = when (sortType) {
            SortType.DESC -> 0
            else -> 1
        }

        fun getSelectIndexByFilterBy(filterBy: FilterBy) = when (filterBy) {
            FilterBy.DATE_TODAY -> 1
            FilterBy.DATE_YESTERDAY -> 2
            FilterBy.DATE_LAST_3_DAYS -> 3
            FilterBy.DATE_LAST_WEEK -> 4
            FilterBy.DATE_LAST_MONTH -> 5
            FilterBy.SIZE_LESS_13 -> 6
            FilterBy.SIZE_13_22 -> 7
            FilterBy.SIZE_22_40 -> 8
            FilterBy.SIZE_40_95 -> 9
            FilterBy.SIZE_MORE_95 -> 10
            FilterBy.STATUS_GOLD -> 11
            FilterBy.STATUS_SILVER -> 12
            else -> 0
        }
    }
}