package com.inmd1.deu_food_gui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.inmd1.deu_food_gui.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), CapabilityClient.OnCapabilityChangedListener {
    var TAG = "wear"
    private val CAPABILITY_NAME = "voice_transcription"

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val watchInfoButton: Button = findViewById(R.id.pair_button)
        watchInfoButton.setOnClickListener {
            // 예시: 워치 정보 조회
            showNodes(CAPABILITY_NAME);
        }

    }

    private fun showNodes(vararg capabilityNames: String) {
        val capabilitiesTask: Task<Map<String, CapabilityInfo>> =
            Wearable.getCapabilityClient(this)
                .getAllCapabilities(CapabilityClient.FILTER_REACHABLE)

        capabilitiesTask.addOnSuccessListener { capabilityInfoMap ->
            val nodes = HashSet<Node>()

            if (capabilityInfoMap.isEmpty()) {
                showDiscoveredNodes(nodes)
                return@addOnSuccessListener
            }

            for (capabilityName in capabilityNames) {
                val capabilityInfo = capabilityInfoMap[capabilityName]
                capabilityInfo?.let {
                    nodes.addAll(it.nodes)
                }
            }

            showDiscoveredNodes(nodes)
        }
    }

    private fun showDiscoveredNodes(nodes: Set<Node>) {
        val nodesList = nodes.map { it.displayName }.toList()
        val connectedNodesMessage =
            if (nodesList.isEmpty()) {
                "No connected device was found for the given capabilities"
            } else {
                nodesList.joinToString(", ")
            }

        val msg = if (nodesList.isNotEmpty()) {
            getString(R.string.connected_nodes, nodesList.joinToString(", "))
        } else {
            getString(R.string.no_device)
        }

       Toast.makeText(this, msg , Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        Wearable.getCapabilityClient(this)
            .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getCapabilityClient(this).removeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true;
    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        Log.d(TAG, capabilityInfo.toString())
    }

}
