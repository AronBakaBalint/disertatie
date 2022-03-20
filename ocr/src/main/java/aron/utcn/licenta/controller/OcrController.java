package aron.utcn.licenta.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aron.utcn.licenta.service.OcrService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OcrController {

	private final OcrService ocrService;
	
	@PostMapping("/ocr")
	public String doOcr(@RequestBody String base64EncodedImage) {
		return ocrService.doOcrOnImage(base64EncodedImage);
	}

}
