package cdmochi.segment.control.nearbymessaging

interface NearbyApi {
    fun publish()
    fun unpublish()
    fun subscribe()
    fun unsubscribe()
}