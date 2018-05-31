package nl.han.ica.examplatform.controllers.decryptioncode

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import nl.han.ica.examplatform.business.decryptioncode.DecryptionCodeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for interaction with decryption code.
 */
@RestController
@RequestMapping("decryptioncode")
class DecryptionCodeController(private val decryptionCodeService: DecryptionCodeService) {

    /**
     * Returns the decryption code to unlock the exam
     *
     * @return [String]
     */
    @GetMapping
    @ApiOperation(
        value = "Returns the decryption code to unlock the exam",
        response = HttpStatus::class
    )
    @ApiResponses(
        ApiResponse(code = 200, message = "Decryption code received"),
        ApiResponse(code = 400, message = "Invalid Answer"),
        ApiResponse(code = 500, message = "Something went wrong")
    )
    fun getDecryptionCode(@RequestBody examId: Int): ResponseEntity<String> =
        decryptionCodeService.getDecryptionCode(examId)
}