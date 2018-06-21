package nl.han.ica.examplatform.controllers.decryptioncode

import io.swagger.annotations.*
import nl.han.ica.examplatform.business.decryptioncode.DecryptionCodeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for interaction with decryption code.
 *
 * @param decryptionCodeService [DecryptionCodeService] The DecryptionCodeService
 */
@RestController
@RequestMapping("decryptioncode")
class DecryptionCodeController(private val decryptionCodeService: DecryptionCodeService) {

    /**
     * Returns the decryption code to unlock the exam
     *
     * @return [String]
     */
    @GetMapping("/{examId}")
    @ApiOperation(
            value = "Returns the decryption code to unlock the exam",
            response = HttpStatus::class
    )
    @ApiResponses(
            ApiResponse(code = 200, message = "Decryption code received"),
            ApiResponse(code = 400, message = "Invalid Answer"),
            ApiResponse(code = 500, message = "Something went wrong")
    )
    fun getDecryptionCode(
            @ApiParam(value = "The ID of the exam you want to retrieve the decryption code for", required = true)
            @PathVariable("examId") examId: Int
    ): ResponseEntity<String> =
            decryptionCodeService.getDecryptionCode(examId)

    /**
     * Returns the decryption code to unlock the exam
     *
     * @return [String]
     */
    @GetMapping()
    @ApiOperation(
            value = "Returns all the decryption codes",
            response = Array<String>::class
    )
    @ApiResponses(
            ApiResponse(code = 200, message = "Decryption code received"),
            ApiResponse(code = 400, message = "Invalid Answer"),
            ApiResponse(code = 500, message = "Something went wrong")
    )
    fun getDecryptionCode(): ResponseEntity<ArrayList<Pair<String, String>>> =
            decryptionCodeService.getAllDecryptionCodes()
}
