package umc.th.juinjang.external.safeSearch;

import java.util.Collections;

import org.springframework.stereotype.Component;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import com.google.cloud.vision.v1.Likelihood;
import com.google.cloud.vision.v1.SafeSearchAnnotation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SafeSearchClient {

	public boolean isSafeImage(String imageUrl,
		Likelihood adultThreshold,
		Likelihood spoofThreshold,
		Likelihood medicalThreshold,
		Likelihood violenceThreshold,
		Likelihood racyThreshold) {
		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			ImageSource imgSource = ImageSource.newBuilder().setImageUri(imageUrl).build();
			Image img = Image.newBuilder().setSource(imgSource).build();

			Feature feature = Feature.newBuilder().setType(Type.SAFE_SEARCH_DETECTION).build();
			AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
				.addFeatures(feature)
				.setImage(img)
				.build();

			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(Collections.singletonList(request));
			AnnotateImageResponse res = response.getResponsesList().get(0);

			if (res.hasError()) {
				throw new RuntimeException("Vision API Error: " + res.getError().getMessage());
			}

			SafeSearchAnnotation annotation = res.getSafeSearchAnnotation();
			log.info("SafeSearch 분석 결과 for [{}]", imageUrl);
			log.info(" - adult: {}", annotation.getAdult());
			log.info(" - spoof: {}", annotation.getSpoof());
			log.info(" - medical: {}", annotation.getMedical());
			log.info(" - violence: {}", annotation.getViolence());
			log.info(" - racy: {}", annotation.getRacy());
			return isAnnotationSafe(annotation, adultThreshold, spoofThreshold, medicalThreshold, violenceThreshold,
				racyThreshold);

		} catch (Exception e) {
			log.error("Vision API 호출 실패 (Exception)", e);
			throw new RuntimeException("Vision API 호출 실패", e);
		}
	}

	public boolean isAnnotationSafe(SafeSearchAnnotation annotation,
		Likelihood adultThreshold,
		Likelihood spoofThreshold,
		Likelihood medicalThreshold,
		Likelihood violenceThreshold,
		Likelihood racyThreshold) {
		return annotation.getAdult().compareTo(adultThreshold) < 0 &&
			annotation.getSpoof().compareTo(spoofThreshold) < 0 &&
			annotation.getMedical().compareTo(medicalThreshold) < 0 &&
			annotation.getViolence().compareTo(violenceThreshold) < 0 &&
			annotation.getRacy().compareTo(racyThreshold) < 0;
	}
}
