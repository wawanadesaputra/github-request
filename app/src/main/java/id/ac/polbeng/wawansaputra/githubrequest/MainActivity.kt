package id.ac.polbeng.wawansaputra.githubrequest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.ac.polbeng.wawansaputra.githubrequest.databinding.ActivityMainBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()
    private val request = OkHttpRequest(client)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetInfo.setOnClickListener {
            val loginID = binding.tvSearch.text.toString()
            if (loginID.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Silahkan masukkan login id akun GitHub Anda!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val url = "https://api.github.com/users/$loginID"
                fetchGitHubInfo(url)
            }
        }
    }

    private fun fetchGitHubInfo(strURL: String) {
        request.GET(strURL, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                println("Request Successful!!")
                println(responseData)

                runOnUiThread {
                    try {
                        if (responseData != null) {
                            val userObject = JSONObject(responseData)
                            val id = userObject.getString("id")
                            val name = userObject.getString("name")
                            val url = userObject.getString("url")
                            val blog = userObject.getString("blog")
                            val bio = userObject.getString("bio")
                            val company = userObject.getString("company")

                            binding.tvUserInfo.text =
                                "${id}\n${name}\n${url}\n${blog}\n${bio}\n${company}"
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Request Failure.")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.tvSearch.setText("")
        binding.tvSearch.hint = "Enter GitHub username"
    }
}
