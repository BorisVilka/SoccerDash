package com.soccer.dash

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*
import kotlin.math.abs

class GameView(val ctx: Context, val att: AttributeSet): SurfaceView(ctx,att) {

    var bg = BitmapFactory.decodeResource(ctx.resources,R.drawable.bg)
    var ball = BitmapFactory.decodeResource(ctx.resources,R.drawable.ball)
    var star = BitmapFactory.decodeResource(ctx.resources,R.drawable.star)
    var fav = BitmapFactory.decodeResource(ctx.resources,R.drawable.fav)
    var fav_bord = BitmapFactory.decodeResource(ctx.resources,R.drawable.bord)
    var borders =  arrayOf(
        BitmapFactory.decodeResource(ctx.resources,R.drawable.border),
        BitmapFactory.decodeResource(ctx.resources,R.drawable.border1),
        BitmapFactory.decodeResource(ctx.resources,R.drawable.border1),
    )
    var music = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("music",true)
    var vibr = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("vibr",true)
    private var paintB: Paint = Paint(Paint.DITHER_FLAG)
    private var paintF: Paint = Paint(Paint.DITHER_FLAG).apply {
        color = ctx.getColor(R.color.bg)
    }
    private var paintT = Paint().apply {
        color = ctx.getColor(R.color.bg)
        textSize = 130f
        style = Paint.Style.FILL
    }
    private var listener: EndListener? = null
    private val random = Random()
    private var millis = 0
    var player = MediaPlayer.create(ctx,R.raw.bg)

    init {
        player.setOnCompletionListener {
            it.start()
        }
        if(music) player.start()
        fav = Bitmap.createScaledBitmap(fav,fav.width/3,fav.height/3,true)
        fav_bord = Bitmap.createScaledBitmap(fav_bord,fav_bord.width/3,fav_bord.height/3,true)
        ball = Bitmap.createScaledBitmap(ball,ball.width/3,ball.height/3,true)
        star = Bitmap.createScaledBitmap(star,star.width/3,star.height/3,true)
        borders[0] = Bitmap.createScaledBitmap(borders[0],borders[0].width/3,borders[0].height/3,true)
        borders[1] = Bitmap.createScaledBitmap(borders[1],borders[1].width/3,borders[1].height/3,true)
        borders[2] = Bitmap.createScaledBitmap(borders[2],borders[2].width/3,borders[2].height/3,true)
        holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {

            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                val canvas = holder.lockCanvas()
                if(canvas!=null) {
                    by = canvas.height-200
                    bx = (canvas.width/2f-ball.width/2f).toInt()
                    draw(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                paused = true
                player.stop()
            }

        })
        val updateThread = Thread {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (!paused) {
                        update.run()
                        millis ++
                    }
                }
            }, 500, 16)
        }

        updateThread.start()
    }
    var code = -1f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action) {
            MotionEvent.ACTION_UP -> {

            }
            MotionEvent.ACTION_DOWN -> {
                code = event.x
                dy = -10
            }
        }
        postInvalidate()
        return true
    }
    var paused = false
    var list = mutableListOf<Model>()
    var delta = 8
    var bx = 0
    var health = 3
    var score = 0
    var by = 0
    var dy = 0

    val update = Runnable{
        var isEnd = false
        var sc = false
        if(paused) return@Runnable
        try {
            val canvas = holder.lockCanvas()
            if(code!=-1f) {
                if(by<=canvas.height-200f) {
                    by += dy
                    if(code>=0) {
                        if(code>bx) bx+=delta
                        else bx-=delta
                    }
                    dy += 2
                } else {
                    by = canvas.height-200
                    code = -1f
                }
            }
            if(bx<=-ball.width) bx = canvas.width
            if(bx>=ball.width+canvas.width) bx = 0
            var i = 0
            while(i<list.size) {
                Log.d("TAG","$i")
                list[i].y+=5
                if(abs(list[i].x-bx) <=ball.width && abs(list[i].y-(by)) <=ball.height) {
                    if(list[i].cur==0) {
                        score++
                        list.removeAt(i)
                    } else {
                        health--
                        sc = true
                        if(health==0) isEnd = true
                        list.removeAt(i)
                        break
                    }
                } else if(list[i].y>canvas.height+ball.height) {
                    list.removeAt(i)
                } else i++
            }
            while(list.size<4) {
                list.add(Model(random.nextInt(canvas.width).toFloat(),-1f*ball.height,random.nextInt(4)))
            }
            canvas.drawBitmap(bg,0f,0f,paintB)
            for(i in list) {
                canvas.drawBitmap(if(i.cur>0) borders[i.cur-1] else star,i.x,i.y,paintB)
            }
            canvas.drawBitmap(ball,bx.toFloat(),by.toFloat(),paintB)
            canvas.drawText(score.toString(),canvas.width/2f,250f,paintT)
            for(i in 0..2) {
                canvas.drawBitmap(if(i<health) fav else fav_bord,canvas.width/2f-100+i*100,50f,paintF)
            }
            holder.unlockCanvasAndPost(canvas)
            if(isEnd) {
                Log.d("TAG","END")
                togglePause()
                if(listener!=null) listener!!.end()
            }
            if(sc) {
                listener?.score(health)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setEndListener(list: EndListener) {
        this.listener = list
    }
    fun togglePause() {
        paused = !paused
    }
    companion object {
        interface EndListener {
            fun end();
            fun score(score: Int);
        }
        data class Model(var x: Float, var y: Float, var cur: Int)
    }
    val b = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("color",0)
}