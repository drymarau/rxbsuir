package by.toggi.rxbsuir.api.internal

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object LocalDateSerializer : KSerializer<LocalDate> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(with(value) { "$dayOfMonth.$monthNumber.$year" })
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val (dayOfMonth, monthNumber, year) = decoder.decodeString()
            .splitToSequence(".")
            .map(String::toInt)
            .toList()
        return LocalDate(year, monthNumber, dayOfMonth)
    }
}
