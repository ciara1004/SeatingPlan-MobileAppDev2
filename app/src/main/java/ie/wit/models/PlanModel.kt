package ie.wit.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class PlanModel(var uid: String? = " ",
                     var tableNo: Int = 0,
                     var guest01: String = " ",
                     var guest02: String = " ",
                     var guest03: String = " ",
                     var guest04: String = " ",
                     var guest05: String = " ",
                     var guest06: String = " ",
                     var guest07: String = " ",
                     var guest08: String = " ",
                     var guest09: String = " ",
                     var guest10: String = " ",
                     var guest11: String = " ",
                     var guest12: String = " ",
                     var profilepic: String = "",
                     var email: String? = "joe@bloggs.com")
                         : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "Table No" to tableNo,
            "Guest 01 " to guest01,
            "Guest 02 " to guest02,
            "Guest 03 " to guest03,
            "Guest 04 " to guest04,
            "Guest 05 " to guest05,
            "Guest 06 " to guest06,
            "Guest 07 " to guest07,
            "Guest 08 " to guest08,
            "Guest 09 " to guest09,
            "Guest 10 " to guest10,
            "Guest 11 " to guest11,
            "Guest 12 " to guest12,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}



