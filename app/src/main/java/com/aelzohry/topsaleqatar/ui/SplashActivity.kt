package com.aelzohry.topsaleqatar.ui

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ActivitySplashBinding
import com.aelzohry.topsaleqatar.helper.AppLanguage
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.ui.ad_details.AdDetailsActivity
import com.aelzohry.topsaleqatar.ui.changeLanguage.ChangeLanguageDialogFragment
import com.aelzohry.topsaleqatar.ui.user.UserFragment
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchReferralInitListener
import io.branch.referral.BranchError
import org.json.JSONObject
import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, BaseViewModel>() {

    override fun getLayoutID(): Int = R.layout.activity_splash

    private var adId: String? = ""
    private var userId: String? = ""

    override fun getViewModel(): BaseViewModel = ViewModelProvider(this)[BaseViewModel::class.java]

    override fun setupUI() {

    }

    override fun onClickedListener() {
    }

    override fun observerLiveData() {

    }

    override fun onStart() {
        super.onStart()
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(if (intent != null) intent.data else null).init()

    }

    private fun goToNextScreen() {
        if (Helper.isFirstRun) {
            askForLanguage()
        } else {
            setHandler()
        }
    }

    private fun setHandler() {
        Handler().postDelayed({
            goToMainScreen()
        }, 2000)
    }

    @SuppressLint("CommitTransaction")
    private fun askForLanguage() {
        val dialog: ChangeLanguageDialogFragment = ChangeLanguageDialogFragment.newInstance()
        supportFragmentManager.beginTransaction().add(dialog, "DialogMessage").commitAllowingStateLoss()
        dialog.setListener { selectedLanguage ->
            var language = selectedLanguage
            if (selectedLanguage == null) {
                val currentLanguage = Locale.getDefault().language
                language = if (currentLanguage == "en") {
                    AppLanguage.English
                } else {
                    AppLanguage.Arabic
                }
            }
            Helper.setLanguage(language)
            Helper.isFirstRun = false
            goToMainScreen()
        }
    }

    private fun goToMainScreen() {
        val mngr = getSystemService(ACTIVITY_SERVICE) as ActivityManager

        val taskList = mngr.getRunningTasks(10)

        if (!TextUtils.isEmpty(adId)) {
            AdDetailsActivity.newInstance(this, null, cameFromChat = false, isClear = true, adId = adId)
        } else if (!TextUtils.isEmpty(userId)) {
            activityLauncher.launch(UserFragment.newInstance(this, null, null, userId))
        } else {
            if (taskList.isEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                if (taskList[0].numActivities == 1 && taskList[0].topActivity!!.className != MainActivity::class.java.name) {
                    Log.i("TAG", "This is last activity in the stack")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        if (intent != null && intent.hasExtra("branch_force_new_session") && intent.getBooleanExtra("branch_force_new_session", false)) {
            Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit()
        }
    }

    private val branchReferralInitListener = BranchReferralInitListener { linkProperties: JSONObject?, error: BranchError? ->
        // do stuff with deep link data (nav to page, display content, etc)
        adId = ""
        userId = ""
        if (linkProperties != null) {
            adId = linkProperties.optString("ad_id")
            userId = linkProperties.optString("user_id")
        }
        goToNextScreen()
    }

}