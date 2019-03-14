
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MemberServiceTest extends AbstractTest {

	// The SUT ----------------
	@Autowired
	private MemberService	memberService;


	// Other services ----------

	// Tests -------------------

	/*
	 * A: Requirement tested: level C: requirement 12.3: The average, the minimum,
	 * the maximum and the standard deviation of the number of members per brotherhood.
	 * C: Analysis of sentence coverage: Sequence
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void dataNumberMemberPerBrotherhood_test() {
		Double[] res;

		res = this.memberService.findDataNumberMembersPerBrotherhood();

		Assert.notNull(res);
		Assert.isTrue(res.length == 4);
	}
}
