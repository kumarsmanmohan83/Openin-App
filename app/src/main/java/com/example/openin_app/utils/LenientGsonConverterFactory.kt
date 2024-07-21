package com.example.openin_app.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.Reader
import java.lang.reflect.Type

class LenientGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return LenientGsonResponseBodyConverter(gson, adapter)
    }

    companion object {
        fun create(): LenientGsonConverterFactory {
            return create(Gson())
        }

        fun create(gson: Gson): LenientGsonConverterFactory {
            return LenientGsonConverterFactory(gson)
        }
    }
}

class LenientGsonResponseBodyConverter<T>(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {
        val responseBodyString = value.string()

        // Check if the response is JSON
        return try {
            if (responseBodyString.trim().startsWith("{")) { // Basic check for JSON
                val jsonReader = gson.newJsonReader(responseBodyString.reader())
                jsonReader.isLenient = true
                adapter.read(jsonReader)
            } else {
                // Handle unexpected content (e.g., HTML)
                throw JsonSyntaxException("Unexpected content: $responseBodyString")
            }
        } catch (e: Exception) {
            throw JsonSyntaxException("Invalid JSON or unexpected content: $responseBodyString", e)
        } finally {
            value.close() // Ensure that the response body is closed
        }
    }
}


