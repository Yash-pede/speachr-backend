package com.speachr.plugins

import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import java.io.File

class AudioProcessor(
    ffmpegPath: String = "/usr/bin/ffmpeg",
    fprobePath: String = "/usr/bin/ffprobe"
) {
    private val ffmpeg = FFmpeg(ffmpegPath)
    private val ffprobe = FFprobe(fprobePath)

    // Reuse this single executor instance globally
    val executor = FFmpegExecutor(ffmpeg, ffprobe)

    fun convert3gpToFlac(inputFile: File, outputFile: File) {
        val builder = FFmpegBuilder().apply {
            overrideOutputFiles(true)
            setInput(inputFile.absolutePath)
            addOutput(outputFile.absolutePath).apply {
                setAudioChannels(1)
                setAudioSampleRate(16_000)
                setAudioCodec("flac")
            }.done()
        }
        executor.createJob(builder).run()
    }

}
