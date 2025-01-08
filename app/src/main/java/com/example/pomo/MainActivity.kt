package com.example.pomo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import android.media.MediaPlayer


class MainActivity : AppCompatActivity() {
    //8)横断的に使うやつを用意
    private lateinit var timer: CountDownTimer
    private var remainingTime: Long = 10000
    private var remainingTime2: Long = 10000
    //入力値保存用の変数をつくる
    private lateinit var mediaPlayer: MediaPlayer // MediaPlayerインスタンス

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //1)開始時間を設定
        val startTime:Long = 10000 //10秒
// タイムアップ音の初期化
        mediaPlayer = MediaPlayer.create(this, R.raw.time_up_sound) // rawフォルダの音ファイルを指定
        //1)viewを取得+tvに開始時間を表示
        val tv:TextView =findViewById(R.id.tv)
        val btnStart:Button =findViewById(R.id.btnStart)
        val btnStop:Button =findViewById(R.id.btnStop)
        val btnRestart:Button =findViewById(R.id.btnRestart)
        val btnReset:Button =findViewById(R.id.btnReset)
        val initialMinutes = (startTime / 1000) / 60
        val initialSeconds = (startTime / 1000) % 60
        tv.text = String.format("%02d:%02d", initialMinutes, initialSeconds)
//
        //2)ボタンの有効・無効
        btnStart.isEnabled = true
        btnStop.isEnabled = false
        btnRestart.isEnabled = false

        //タイマー時間をタッチしたら編集できるように
        tv.isClickable = true
        tv.setOnClickListener {
            showEditTimeDialog()
        }
        //3)スタートボタンを押したらカウントダウン
        btnStart.setOnClickListener {
            //5)「４」のtimerをスタート
            //timer.start()
            //10)startTimeでカウントダウン開始
            startsyori()
        }

        //6)ストップボタンを押した時の処理
        btnStop.setOnClickListener {
            timer.cancel()

            //ストップボタンを無効化
            btnStart.isEnabled = true
            btnStop.isEnabled = false
            btnRestart.isEnabled = true
        }

        //7)リスタートボタンを押した時の処理
        btnRestart.setOnClickListener {
            //10)remainingTimeでカウントダウン開始
            timer = countDownTimer(remainingTime).start()

            btnStart.isEnabled = false
            btnStop.isEnabled = true
            btnRestart.isEnabled = false
        }

        // リセットボタンを押した時の処理
        btnReset.setOnClickListener {
            timer.cancel()
            remainingTime = 0
            tv.text = String.format("%02d:%02d", startTime / 1000 / 60, startTime / 1000 % 60) // 開始時間を分:秒で表示

            // ボタンの有効・無効
            btnStart.isEnabled = true
            btnStop.isEnabled = false
            btnRestart.isEnabled = false
        }

    }
    private fun showEditTimeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_time, null)
        val etMinutes = dialogView.findViewById<EditText>(R.id.etMinutes)
        val etSeconds = dialogView.findViewById<EditText>(R.id.etSeconds)
        val etMinutes2 = dialogView.findViewById<EditText>(R.id.etMinutes2)
        val etSeconds2 = dialogView.findViewById<EditText>(R.id.etSeconds2)
        val tv:TextView =findViewById(R.id.tv)

        AlertDialog.Builder(this)
            .setTitle("タイマー時間を編集")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val minutes = etMinutes.text.toString().toLongOrNull() ?: 0
                val seconds = etSeconds.text.toString().toLongOrNull() ?: 0
                val minutes2 = etMinutes2.text.toString().toLongOrNull() ?: 0
                val seconds2 = etSeconds2.text.toString().toLongOrNull() ?: 0
                val newTime = (minutes * 60 + seconds) * 1000
                val newTime2 = (minutes2 * 60 + seconds2) * 1000



                remainingTime = newTime
                remainingTime2 = newTime2

                val formattedMinutes = newTime / 1000 / 60
                val formattedSeconds = newTime / 1000 % 60
                val formattedMinutes2 = newTime2 / 1000 / 60
                val formattedSeconds2 = newTime2 / 1000 % 60
                tv.text = String.format("%02d:%02d", formattedMinutes, formattedSeconds)

            }
            .setNegativeButton("キャンセル", null)
            .show()
    }

    //8)「４」を関数でここに用意
    private fun countDownTimer(st: Long): CountDownTimer {
        val tv: TextView = findViewById(R.id.tv)
        val btnStart: Button = findViewById(R.id.btnStart)
        val btnStop: Button = findViewById(R.id.btnStop)
        val btnRestart: Button = findViewById(R.id.btnRestart)

        return object : CountDownTimer(st, 100) {
            override fun onTick(p0: Long) {
                val minutes = (p0 / 1000) / 60
                val seconds = (p0 / 1000) % 60
                tv.text = String.format("%02d:%02d", minutes, seconds)
                remainingTime = p0
            }

            override fun onFinish() {
                tv.text = "タイムアップ"
                btnStart.isEnabled = false
                btnStop.isEnabled = false
                btnRestart.isEnabled = false

                // タイムアップ音を再生
                // MediaPlayerを適切にリセットして再生
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }
                mediaPlayer.reset() // MediaPlayerをリセット
                mediaPlayer = MediaPlayer.create(applicationContext, R.raw.time_up_sound) // 再作成
                mediaPlayer.start()

                // ポップアップメッセージを表示
                showTimeUpDialog()
            }
        }
    }
    private fun countDownTimer2(st: Long): CountDownTimer {
        val tv: TextView = findViewById(R.id.tv)
        val btnStart: Button = findViewById(R.id.btnStart)
        val btnStop: Button = findViewById(R.id.btnStop)
        val btnRestart: Button = findViewById(R.id.btnRestart)

        return object : CountDownTimer(st, 100) {
            override fun onTick(p2: Long) {
                val minutes = (p2 / 1000) / 60
                val seconds = (p2 / 1000) % 60
                tv.text = String.format("%02d:%02d", minutes, seconds)
                remainingTime2 = p2
            }

            override fun onFinish() {
                tv.text = "タイムアップ"
                btnStart.isEnabled = false
                btnStop.isEnabled = false
                btnRestart.isEnabled = false

                // タイムアップ音を再生
                // タイムアップ音を再生
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }
                mediaPlayer.release()
                mediaPlayer = MediaPlayer.create(applicationContext, R.raw.time_up_sound)
                mediaPlayer.start()
                showTimeUpDialog2()

            }
        }
    }
    private fun startsyori() {
        val btnStart:Button =findViewById(R.id.btnStart)
        val btnStop:Button =findViewById(R.id.btnStop)
        val btnRestart:Button =findViewById(R.id.btnRestart)
        val btnReset:Button =findViewById(R.id.btnReset)
        val tv:TextView =findViewById(R.id.tv)
        timer = countDownTimer(remainingTime).start()

        btnStart.isEnabled = false
        btnStop.isEnabled = true
        btnRestart.isEnabled = false
    }
    private fun startsyori2() {
        val btnStart:Button =findViewById(R.id.btnStart)
        val btnStop:Button =findViewById(R.id.btnStop)
        val btnRestart:Button =findViewById(R.id.btnRestart)
        val btnReset:Button =findViewById(R.id.btnReset)
        val tv:TextView =findViewById(R.id.tv)

        timer = countDownTimer2(remainingTime2).start()
        tv.text = String.format("%02d:%02d", remainingTime2 / 1000 / 60, remainingTime2/ 1000 % 60)

        btnStart.isEnabled = false
        btnStop.isEnabled = true
        btnRestart.isEnabled = false
    }
    //stopの処理関数
    private fun stopsyori() {
        val btnStart:Button =findViewById(R.id.btnStart)
        val btnStop:Button =findViewById(R.id.btnStop)
        val btnRestart:Button =findViewById(R.id.btnRestart)
        val btnReset:Button =findViewById(R.id.btnReset)
        val tv:TextView =findViewById(R.id.tv)
        val startTime:Long = 10000 //10秒
        // MediaPlayerを停止してリリースし、再作成
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(this, R.raw.time_up_sound)
        timer.cancel()
        remainingTime = 0
        tv.text = String.format("%02d:%02d", startTime / 1000 / 60, startTime / 1000 % 60) // 開始時間を分:秒で表示
        btnStart.isEnabled = true
        btnStop.isEnabled = false
        btnRestart.isEnabled = false
    }
    private fun showTimeUpDialog() {
        AlertDialog.Builder(this)
            .setTitle("タイマー終了")
            .setMessage("時間になりました！")
            .setPositiveButton("OK") { dialog, _ ->

                dialog.dismiss()
                stopsyori()
                startsyori2()

            }
            .show()
    }
    private fun showTimeUpDialog2() {
        AlertDialog.Builder(this)
            .setTitle("作業終了！")
            .setMessage("1年後を想像して行動しよう！")//waw
            .setPositiveButton("OK") { dialog, _ ->

                dialog.dismiss()
                stopsyori()

            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // MediaPlayerのリソース解放
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}