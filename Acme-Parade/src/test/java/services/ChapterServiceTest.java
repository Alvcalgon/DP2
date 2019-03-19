
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Area;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChapterServiceTest extends AbstractTest {

	// Services under test

	@Autowired
	private ChapterService	chapterService;

	@Autowired
	private AreaService		areaService;


	// Test

	/**
	 * Requirement tested: Req2 (B-level), 1: Self-assign an area to co-ordinate. Once an area is self-assigned, it cannot be changed.
	 * Le asignamos a un chapter (que ya coordina un area),un area (que ya está coordinada)
	 * Analysis of sentence coverage: 23%(3 lines /13 lines)
	 * Analysis of data coverage: Intentionally blank
	 */
	@Test(expected = IllegalArgumentException.class)
	public void selfAssignedArea_negative_test() {
		super.authenticate("chapter1");

		int areaId;
		Area area;

		areaId = super.getEntityId("area1");
		area = this.areaService.findOne(areaId);

		this.chapterService.selfAssignedArea(area);

		super.unauthenticate();
	}

	/**
	 * Requirement tested: Req2 (B-level), 1: Self-assign an area to co-ordinate. Once an area is self-assigned, it cannot be changed.
	 * Le asignamos a un chapter (que ya coordina un area),un area (sin coordinar)
	 * Analysis of sentence coverage: 76.9%(10 lines /13 lines)
	 * Analysis of data coverage: Intentionally blank
	 */
	@Test(expected = IllegalArgumentException.class)
	public void selfAssignedArea_negative2_test() {
		super.authenticate("chapter1");

		int areaId;
		Area area;

		areaId = super.getEntityId("area3");
		area = this.areaService.findOne(areaId);

		this.chapterService.selfAssignedArea(area);

		super.unauthenticate();
	}

	/**
	 * Requirement tested: Req2 (B-level), 1: Self-assign an area to co-ordinate. Once an area is self-assigned, it cannot be changed.
	 * Le asignamos a un chapter (que no coordina area),un area (ya coordinada por otro chapter)
	 * Analysis of sentence coverage: 23%(3 lines /13 lines)
	 * Analysis of data coverage: Intentionally blank
	 */
	@Test(expected = IllegalArgumentException.class)
	public void selfAssignedArea_negative3_test() {
		super.authenticate("chapter3");

		int areaId;
		Area area;

		areaId = super.getEntityId("area1");
		area = this.areaService.findOne(areaId);

		this.chapterService.selfAssignedArea(area);

		super.unauthenticate();
	}

	/**
	 * Requirement tested: Req2 (B-level), 1: Self-assign an area to co-ordinate. Once an area is self-assigned, it cannot be changed.
	 * Analysis of sentence coverage: 54.5%(6 lines /11 lines)
	 * Analysis of data coverage: Intentionally blank
	 */
	@Test
	public void selfAssignedArea_positive_test() {
		super.authenticate("chapter3");

		int areaId;
		Area area;

		areaId = super.getEntityId("area3");
		area = this.areaService.findOne(areaId);

		this.chapterService.selfAssignedArea(area);

		super.unauthenticate();
	}

}
