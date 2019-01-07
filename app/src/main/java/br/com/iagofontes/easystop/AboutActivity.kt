package br.com.iagofontes.easystop

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.R.attr.versionName
import android.content.pm.PackageInfo
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        txtDevsName.text = "Iago Fontes e Felipe Izidoro"
        txtVersionName.text = this.getAppVersion()+"v"

    }

    private fun getAppVersion(): String {
        try {
            val pInfo = applicationContext.getPackageManager().getPackageInfo(packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "0.0"
    }


}
