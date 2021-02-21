package org.tridzen.mamafua.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_confirm.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.network.withdi.Resource
import org.tridzen.mamafua.ui.auth.LoginFragment
import org.tridzen.mamafua.ui.home.order.prepare.cart.CartFragment
import java.util.*
import kotlin.math.ln

private const val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"

fun calculateNoOfColumns(
    context: Context,
    columnWidthDp: Float
): Int { // For example columnWidthdp=180
    val displayMetrics = context.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

//fun Fragment.addMenu(toolbar: Toolbar) {
//    toolbar.inflateMenu(R.menu.menu_cart)
//    toolbar.setOnMenuItemClickListener {
//        findNavController().navigate(R.id.destination_cart)
//        true
//    }
//}

fun View.showSnackBar(text: CharSequence, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, text, length).show()
}

fun Context.showToast(text: CharSequence, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, length).show()
}

//fun Fragment.setUpFragmentMap(resId: Int) {
//    if (this is OnMapReadyCallback) {
//        val fm = childFragmentManager
//        val supportMapFragment = SupportMapFragment.newInstance()
//        fm.beginTransaction().replace(resId, supportMapFragment)
//            .commit()
//        supportMapFragment.getMapAsync(this)
//    } else {
//        throw MapCallBackException("You should implement OnMapReadCallBack")
//    }
//}
//
//fun Activity.getLocation(onLatLangListener: OnLatLangListener) {
//    val mFusedLocationClient: FusedLocationProviderClient =
//        LocationServices.getFusedLocationProviderClient(this)
//
//    mFusedLocationClient.lastLocation
//        .addOnSuccessListener {
//            it?.let {
//                onLatLangListener.onSuccess(LatLng(it.latitude, it.longitude))
//            }
//        }
//        .addOnFailureListener { e ->
//            e.printStackTrace()
//            onLatLangListener.onFailure(e.message)
//        }
//}

fun Context.zoomMapToRadius(
    latitude: Double,
    longitude: Double,
    radius: Double,
    googleMap: GoogleMap
) {
    val position = LatLng(latitude, longitude)
    val center = CameraUpdateFactory.newLatLng(position)
    googleMap.moveCamera(center)
    val zoom = CameraUpdateFactory.zoomTo(getZoomLevel(radius))
    googleMap.animateCamera(zoom)
}

fun Context.getZoomLevel(radius: Double): Float {
    return if (radius > 0) {
        val metrics = resources.displayMetrics
        val size = if (metrics.widthPixels < metrics.heightPixels) metrics.widthPixels
        else metrics.heightPixels
        val scale = radius * size / 300000
        (16 - ln(scale) / ln(2.0)).toFloat()
    } else 16f
}

fun addMarkers(locations: List<LatLng>, map: GoogleMap, context: Context, drawable: Int?) {
    locations.forEach {
        map.addMarker(
            MarkerOptions().position(it).icon(
                bitmapDescriptorFromVector(
                    context,
                    drawable
                )
            )
        )
    }
}

fun bitmapDescriptorFromVector(context: Context, vectorResId: Int?): BitmapDescriptor {
    val vectorDrawable = vectorResId?.let { ContextCompat.getDrawable(context, it) }
    vectorDrawable!!.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun RecyclerView.runLayoutAnimation() {
    val controller =
        AnimationUtils.loadLayoutAnimation(context, R.anim.list_animation)

    layoutAnimation = controller
    adapter?.notifyDataSetChanged()
    scheduleLayoutAnimation()
}

fun randomString(): String {
    val randSize = 24
    val random = Random()
    val sb = StringBuilder(randSize)
    for (i in 0 until randSize)
        sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
    return sb.toString()
}

fun firstLetter(s: String) = s.substring(0, 1)

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.NewInstanceFactory): T {
    return ViewModelProvider(this, factory)[T::class.java]
}

inline fun <reified T : ViewModel> AppCompatActivity.getActivityViewModel(factory: ViewModelProvider.NewInstanceFactory): T {
    return ViewModelProvider(this, factory)[T::class.java]
}

inline fun <T> tryOrNull(block: () -> T): T? =
    try {
        block()
    } catch (e: Throwable) {
        null
    }

fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun <F : Activity> Fragment.startNewActivity(activity: Class<F>) {
    Intent(requireContext(), activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.handleApiError(
    failure: org.tridzen.mamafua.data.remote.network.current.Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> requireView().snackbar(
            "Please check your internet connection",
            retry
        )
        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackbar("You've entered incorrect email or password")
            } else {
//                (this as BaseFragment<*, *, *>).logout()
            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }
    }
}

fun Fragment.send() {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun getName(profile: Profile) = profile.firstName + " " + profile.lastName

fun String.validateNumber(): Boolean =
    this.matches(Regex("(\\+?254|0|^)[-. ]?[7,1]([0-2,4 9][0-9]|[9][0-2])[0-9]{6}\\z"))

fun <T, A> performGetOperation(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val source = databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Resource.Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)

        } else if (responseStatus.status == Resource.Status.ERROR) {
            emit(Resource.error(responseStatus.message!!))
            emitSource(source)
        }
    }

fun hideView(vararg views: View, condition: Boolean) {
    views.forEach {
        it.visible(condition)
    }
}

fun Context.showMaterialDialog(
    titleText: String,
    contentText: String,
    animationId: Int? = R.raw.caution,
    showAnim: Boolean? = false,
    buttonConfirm: String = "Confirm",
    buttonCancel: String = "Cancel",
    function: () -> Any
): MaterialDialog {
    return MaterialDialog(this).show {
        customView(R.layout.dialog_confirm)
        val view = getCustomView()
        val confirm = view.butConfirm
        val cancel = view.butCancel
        val heading = view.tvWarning
        val content = view.tvMessage

        if (animationId != null) {
            view.lavPurchase.setAnimation(animationId)
        }

        view.lavPurchase.visibility = if (showAnim == true) View.VISIBLE else View.GONE

        confirm.text = buttonConfirm
        cancel.text = buttonCancel

        content.text = titleText
        heading.text = contentText

        confirm.setOnClickListener {
            function()
            dismiss()
        }

        cancel.setOnClickListener {
            dismiss()
        }
    }
}

fun <T : CoordinatorLayout.Behavior<*>> View.findBehavior(): T = layoutParams.run {
    if (this !is CoordinatorLayout.LayoutParams) throw IllegalArgumentException("View's layout params should be CoordinatorLayout.LayoutParams")

    (layoutParams as CoordinatorLayout.LayoutParams).behavior as? T
        ?: throw IllegalArgumentException("Layout's behavior is not current behavior")
}

suspend fun getLatLng(context: Context): CartFragment.LatLng {
    val latitude =
        AppPreferences(context).getValue(AppPreferences.LATITUDE_PREFS)
            .first()

    val longitude =
        AppPreferences(context).getValue(AppPreferences.LONGITUDE_PREFS)
            .first()

    return if (latitude != null && longitude != null)
        CartFragment.LatLng(latitude, longitude)
    else CartFragment.LatLng(0.0, 0.0)
}

