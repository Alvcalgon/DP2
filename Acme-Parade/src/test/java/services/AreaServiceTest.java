
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AreaServiceTest extends AbstractTest {

	// The SUT ----------------------------------------------------------------

	@Autowired
	private AreaService	areaService;


	// Other services ---------------------------------------------------------

	// Tests ------------------------------------------------------------------

	/*
	 * Requirement tested: Acme-Parade Req 8.1 (C-level): The ratio of areas that are not co-ordinated by any chapter.
	 * Analysis of sentence coverage: 100%
	 * Analysis of data coverage: Intentionally blank 100%
	 */
	@Test
	public void testRatioAreaWithoutChapter() {
		Double data;

		data = this.areaService.ratioAreaWithoutChapter();

		Assert.isTrue(data == 0.66667);
	}

}
