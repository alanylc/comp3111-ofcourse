package test;

import static org.junit.Assert.*;

import ofcourse.Network;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
//Account used in this test:
//{username,password}
//{ctestcaa,aaa},{ctestcab,bbb},{ctestcac,ccc}
public class NetworkTest {
	String[] username={"ctestcaa","ctestcab","ctestcac"};
	String[] password={"aaa","bbb","ccc"};
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Network.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//Network.initialize();
	}

	@Before
	public void setUp() throws Exception {
		Network.login("u", "p");
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOurNetwork() {
		Network.getOurNetwork();
		assertEquals("u",Network.getOurNetworkUserName());
		assertEquals("b3a54611f6b44141ce5c7884c352b2a7f7128cbc486fdf058d87144e0d8a2ce9",Network.getOurNetworkPassword());
	}

	@Test
	public void testLogin() {
		Network.login("ctestcaa", "aaa");
		assertEquals("ctestcaa",Network.getOurNetworkUserName());
		assertEquals("02152f5107ca07748ad82b4af6e12dcef62910b43a7b2581dbb83b37cb89a61e",Network.getOurNetworkPassword());
	}

	@Test
	public void testLogout() {
		Network.logout();
		assertEquals("",Network.getOurNetworkUserName());
		assertEquals("",Network.getOurNetworkPassword());
	}

	@Test
	public void testDataCut() {//case for 4*2 items
		String test="ItemA1ItemA24974656d4133ItemA4ItemB1ItemB24974656d4233ItemB4";
		Network a=Network.getOurNetwork();
		String[][] result=a.dataCut(test, 4);
		String[][] expected={{"ItemA1","ItemA2","ItemA3","ItemA4"},{"ItemB1","ItemB2","ItemB3","ItemB4"}};
		assertArrayEquals(expected, result);
	}
	@Test
	public void testDataCut2() {//case for 2*4 items
		String test="ItemA1ItemA2ItemB1ItemB2ItemC1ItemC2ItemD1ItemD2";
		Network a=Network.getOurNetwork();
		String[][] result=a.dataCut(test, 2);
		String[][] expected={{"ItemA1","ItemA2"},{"ItemB1","ItemB2"},{"ItemC1","ItemC2"},{"ItemD1","ItemD2"}};
		assertArrayEquals(expected, result);
	}
	/*
	@Test
	public void testPrintArray() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testFromHexString() {
		Network a=Network.getOurNetwork();
		String expected="Item";
		assertEquals(expected,a.fromHexString("4974656D"));
	}
/*
	@Test
	public void testRegister() {//we don't test Register because we don't want to send email
		Network a=Network.getOurNetwork();
		String expected="100";//
		assertEquals(expected,a.register("testcae"));//register may take up to 15 seconds due to time needed to send email and wait for server reply
	}
	@Test
	public void testRegister2() {//we don't test Register because we don't want to send email
		Network a=Network.getOurNetwork();
		String expected="003";//Currently it is not possible to replicate 003 on client side
		assertEquals(expected,a.register("testcae"));//register may take up to 15 seconds due to time needed to send email and wait for reply
	}
	*/
	@Test
	public void testRegisterB() {//Case for successful register
		Network a=Network.getOurNetwork();
		String expected="100";
		assertEquals(expected,a.registerB("testcad"));
	}
	@Test
	public void testRegisterB2() {//Case for duplicate entries
		Network a=Network.getOurNetwork();
		String expected="001";
		assertEquals(expected,a.registerB("testcad"));
	}
	@Test
	public void testRegisterB3() {//Case for invalid characters (special char on username)
		Network a=Network.getOurNetwork();
		String expected="000";
		assertEquals(expected,a.registerB("test=cad"));
	}
	@Test
	public void testGetMyFav() {//case for successful return
		Network a=Network.getOurNetwork();
		Network.login(username[0], password[0]);
		String expected="Fav";
		assertEquals(expected,a.getMyFav());
	}
	@Test
	public void testGetMyFav2() {//case for successful return empty
		Network a=Network.getOurNetwork();
		Network.login(username[1], password[1]);
		String expected="";
		assertEquals(expected,a.getMyFav());
	}
	@Test
	public void testGetMyFav3() {//case for wrong username/password
		Network a=Network.getOurNetwork();
		Network.login(username[0], password[1]);
		String expected="002";
		assertEquals(expected,a.getMyFav());
	}
	@Test
	public void testGetMyFav4() {//case for logged out
		Network a=Network.getOurNetwork();
		Network.logout();
		String expected="200";
		assertEquals(expected,a.getMyFav());
	}

	@Test
	public void testGetFriendList() {//case for successful return as personA
		Network a=Network.getOurNetwork();
		Network.login(username[0], password[0]);
		String expected="ctestcab!";
		assertEquals(expected,a.getFriendList());
	}
	@Test
	public void testGetFriendList2() {//case for successful return as personB
		Network a=Network.getOurNetwork();
		Network.login(username[1], password[1]);
		String expected="ctestcaa!";
		assertEquals(expected,a.getFriendList());
	}
	@Test
	public void testGetFriendList3() {//case for successful return as empty
		Network a=Network.getOurNetwork();
		Network.login(username[2], password[2]);
		String expected="";
		assertEquals(expected,a.getFriendList());
	}
	@Test//caa sent a friend to cac before this test
	public void testGetReqFriendList() {
		Network a=Network.getOurNetwork();
		Network.login(username[0], password[0]);
		String expected="testcad!ctestcac!";//TODO this should not show cac
		assertEquals(expected,a.getReqFriendList());
	}
	@Test
	public void testGetReqFriendList2() {
		Network a=Network.getOurNetwork();
		Network.login(username[2], password[2]);
		String expected="ctestcaa!";
		assertEquals(expected,a.getReqFriendList());
	}
/*Not able to test
	@Test
	public void testFirstNewPW() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testNewPW() {//case when work perfectly
		Network a=Network.getOurNetwork();
		Network.login(username[2], password[2]);
		String expected="100";
		assertEquals(expected,a.newPW(password[2], "ddd"));
		expected="ctestcaa!";
		assertEquals(expected,a.getReqFriendList());//chk if immediately usable
		Network.login(username[2], "ddd");
		assertEquals(expected,a.getReqFriendList());//chk if usable after login
		expected="100";
		assertEquals(expected,a.newPW( "ddd",password[2]));//change it back
	}

	@Test
	public void testChkFirstPW() {//case for firstPw = true
		Network a=Network.getOurNetwork();
		Network.login("testcad","");
		boolean expected=true;
		assertEquals(expected,a.chkFirstPW());
	}
	@Test
	public void testChkFirstPW2() {//case for firstPw = false
		Network a=Network.getOurNetwork();
		Network.login(username[2], password[2]);
		boolean expected=false;
		assertEquals(expected,a.chkFirstPW());
	}
	@Test
	public void testComment() {//case for successful commenting
		Network a=Network.getOurNetwork();
		Network.login(username[2], password[2]);
		String Course = "COMP3111";
		String Grade = "4";
		String Comment = "更ら戳";
		String expected="100";
		assertEquals(expected,a.comment(Course, Grade, Comment));
	}
	@Test
	public void testComment2() {//case for failure due to not logged in
		Network a=Network.getOurNetwork();
		Network.logout();
		String Course = "COMP3111";
		String Grade = "4";
		String Comment = "更ら戳";
		String expected="200";
		assertEquals(expected,a.comment(Course, Grade, Comment));
	}
	@Test
	public void testGetSummary() {//case for logged in
		Network a=Network.getOurNetwork();
		Network.login(username[2], password[2]);
		String Course="COMP3111";
		String[][] expected={{"COMP3111","3.7500"}};
		assertArrayEquals(expected,a.getSummary(Course));
	}
	@Test
	public void testGetSummary2() {//case for not logged in
		Network a=Network.getOurNetwork();
		Network.logout();
		String Course="COMP3111";
		String[][] expected={{"COMP3111","3.7500"}};
		assertArrayEquals(expected,a.getSummary(Course));
	}
	@Test
	public void testGetCourse() {
		Network a=Network.getOurNetwork();
		Network.login(username[2], password[2]);
		String Course="COMP3111";
		String[][] expected={{"ctestcac","4","更ら戳"},{"ctestcaa","4","更ら戳"}};
		String[][] actual=a.getCourse(Course);
		String[][] actual2={{actual[0][0],actual[0][1],actual[0][2]},{actual[1][0],actual[1][1],actual[1][2]}};
		assertArrayEquals(expected,actual2);
	}
	@Test
	public void testFriendReq() {//case when logged in
		Network a=Network.getOurNetwork();
		Network.login(username[0], password[0]);
		String expected="100";
		assertEquals(expected,a.friendReq("testcad"));
	}
	@Test
	public void testFriendReq2() {//case when not logged in
		Network a=Network.getOurNetwork();
		Network.logout();
		String expected="200";
		assertEquals(expected,a.friendReq(username[0]));
	}
	@Test
	public void testFriendReq3() {//case when logged in,duplicate entry
		Network a=Network.getOurNetwork();
		Network.login(username[0], password[0]);
		String expected="004";
		assertEquals(expected,a.friendReq(username[2]));
	}
	@Test
	public void testFriendSet() {//case when logged in
		Network a=Network.getOurNetwork();
		Network.login(username[0], password[0]);
		String expected="100";
		assertEquals(expected,a.friendSet("testcad"));
	}
	@Test
	public void testFriendSet2() {//case when not logged in
		Network a=Network.getOurNetwork();
		Network.logout();
		String expected="200";
		assertEquals(expected,a.friendSet("testcad"));
	}
	@Test
	public void testFriendSet3() {//case when duplicated set, sorry, I can't catch this error on server side
		Network a=Network.getOurNetwork();
		Network.login(username[0], password[0]);
		String expected="100";
		assertEquals(expected,a.friendSet("testcad"));
	}
	@Test
	public void testFriendSet4() {//case when set to a non-existent friend request
		Network a=Network.getOurNetwork();
		Network.login(username[0], password[0]);
		String expected="005";
		assertEquals(expected,a.friendSet("testcaf"));
	}
	@Test
	public void testSetMyFav() {//case when set successfully
		Network a=Network.getOurNetwork();
		Network.login(username[1], password[1]);
		String expected="100";
		assertEquals(expected,a.setMyFav("favv"));
		expected="favv";
		assertEquals(expected,a.getMyFav());
		a.setMyFav("");
	}
	@Test
	public void testSetMyFav2() {//case when not logged in
		Network a=Network.getOurNetwork();
		Network.logout();
		String expected="200";
		assertEquals(expected,a.setMyFav("favv"));
	}
	/*
	@Test
	public void testTestPassword() {
		fail("Not yet implemented");
	}
*/
}
