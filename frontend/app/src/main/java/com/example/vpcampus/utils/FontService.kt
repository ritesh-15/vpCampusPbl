package com.example.vpcampus.utils

import android.content.Context
import android.graphics.Typeface

class FontService {

    companion object{

        fun regular(context:Context):Typeface{
            return Typeface.createFromAsset(context.assets,"Poppins-Regular.ttf")
        }

        fun bold(context:Context):Typeface{
            return Typeface.createFromAsset(context.assets,"Poppins-Bold.ttf")
        }

        fun semiBold(context: Context):Typeface{
            return Typeface.createFromAsset(context.assets,"Poppins-SemiBold.ttf")
        }

        fun light(context: Context):Typeface{
            return Typeface.createFromAsset(context.assets,"Poppins-Light.ttf")
        }

        fun extraBold(context: Context):Typeface{
            return Typeface.createFromAsset(context.assets,"Poppins-ExtraBold.ttf")
        }
    }

}