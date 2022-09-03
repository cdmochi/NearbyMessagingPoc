package cdmochi.nearbymessaging

interface NearbyApi {
    fun publish()
    fun unpublish()
    fun subscribe()
    fun unsubscribe()
}