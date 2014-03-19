package ofcourse;

public enum Response {
	_100(100), _404(404), _000(000), _001(001), _002(002), _003(003), _004(004), _005(005), _666(666);
	//000 char error
	//001 Name registered
	//002 wrong Name/pw
	//003 email not sent
	//004 duplicate entry when insert 
	//005 entry not exist when update
	//404 Query return false: SQL server connection failed
	//666 unknown error
	//100 OK
	
	private int code = 100;
	
	private Response (int code){
		this.code = code;
	}
		
	public static Response fromCode(String code) {
		try {
			return Response.valueOf("_"+ code);
		}
		catch (Exception e) {
			return Response.valueOf("_"+ code);
		}
		
	}
	
	public boolean isError() {
		return code != 100;
	}
	
	public String messageDefaultString() {
		switch (code) {
		case 0:
			return "Character error. PLease avoid invalid characters as specified, " +
					"those that cannot normally typed by keyboard and IME, or copy text with unknown encoding.";
		case 1:
			//TODO: Reset password instead.
			return "This ITSC is already registered.";
		case 2:
			return "ITSC name and/or password is wrong";
		case 3:
			//TODO: Ask for fallback to offline 
			return "Email containg the password cannot be sent, possibly due to UST mail server.";
		case 4:
			return "This entry already exists.";
		case 5:
			return "This entry does not exist.";
		case 404:
			//TODO: Ask for fallback to offline 
			return "SQL server error.";
		case 666:
			return "Unkown error.";
		default:
			return "OK.";
			
		}
	}
	
	
}
