package utils

data class TorrentInfo (
    var name: String,
    var sizeMb: Double,
    var seeds: Int,
    var peers: Int,
    var status: Status,
    var comments: Int
)

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
    }
}