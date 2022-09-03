package cdmochi.nearbymessaging

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cdmochi.nearbymessaging.databinding.ActivityMainBinding
import cdmochi.nearbymessaging.widget.NearbyDiscoveryAdapter
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import java.nio.charset.Charset

class MainActivity : AppCompatActivity(), NearbyApi {

    private lateinit var binding: ActivityMainBinding

    private val TTL_IN_SECONDS = Strategy.TTL_SECONDS_INFINITE
    private val PUBLISH_SUBSCRIBE_STRATEGY = Strategy.Builder()
        .setTtlSeconds(TTL_IN_SECONDS)
        .build()

    private lateinit var publishMessage: Message
    private lateinit var subscribeMessageListener: MessageListener

    private val nearbyAdapter by lazy {
        NearbyDiscoveryAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        publishMessage = Message(Build.MODEL.toByteArray(Charset.forName("UTF-8")))
        subscribeMessageListener = object: MessageListener() {
            override fun onFound(messagePayload: Message) {
                val message = String(messagePayload.content)
                nearbyAdapter.addItem(message)
                Log.d("nearbyMessage", "LISTEN: $message")
            }

            override fun onLost(messagePayload: Message) {
                val message = String(messagePayload.content)
                nearbyAdapter.removeItem(message)
            }
        }

        binding.publishSwitch.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked)
                publish()
            else
                unpublish()
        }

        binding.subscribeSwitch.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked)
                subscribe()
            else
                unsubscribe()
        }
        setupMessageDisplay()
    }

    private fun setupMessageDisplay() {
        with(binding.nearbyMsgRecyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = nearbyAdapter
        }
    }

    override fun publish() {
        val options = PublishOptions.Builder()
            .setStrategy(PUBLISH_SUBSCRIBE_STRATEGY)
            .setStrategy(Strategy.DEFAULT)
            .setCallback(object: PublishCallback() {
                override fun onExpired() {
                    super.onExpired()
                    runOnUiThread { binding.publishSwitch.isChecked = false }
                }
            })
            .build()

        Nearby
            .getMessagesClient(this)
            .publish(publishMessage, options)
            .addOnSuccessListener {
                Toast.makeText(this, "Published", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Publishe failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun unpublish() {
        Nearby.getMessagesClient(this).unpublish(publishMessage)
    }

    override fun subscribe() {
        val options = SubscribeOptions.Builder()
            .setStrategy(PUBLISH_SUBSCRIBE_STRATEGY)
            .setStrategy(Strategy.DEFAULT)
            .setCallback(object: SubscribeCallback() {
                override fun onExpired() {
                    super.onExpired()
                    runOnUiThread { binding.subscribeSwitch.isChecked = false }
                }
            })
            .build()

        Nearby
            .getMessagesClient(this)
            .subscribe(subscribeMessageListener, options)
            .addOnSuccessListener {
                Toast.makeText(this, "subscribed", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "subscribe failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun unsubscribe() {
        Nearby
            .getMessagesClient(this)
            .unsubscribe(subscribeMessageListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        unpublish()
        unsubscribe()
    }
}