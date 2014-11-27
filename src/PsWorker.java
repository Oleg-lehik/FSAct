import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

	/**
	 * Special class for bash controlling and executing any bash command.
	 */
		class PsWorker {
			
			/**
			 * Executes bash command.
			 * @param command - command to execute
			 * @param result - resulting output of executor
			 * @return output result of execution - generic array of strings
			 */
		ArrayList<String> executeCommand(String command,ArrayList<String> result){
		result.clear();
		File wd = new File("/bin");
		Process proc = null;
		try {
		   proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
		}
		catch (IOException e) {
		   e.printStackTrace();
		   return null;
		}
		if (proc != null) {
		   BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		   PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
		   out.println(command);
		   System.out.println("Command: "+ command+" executed...");
		   out.println("exit");
		   try {
		      String line;
		      while ((line = in.readLine()) != null) {
		    	 if (result.size()<199999) result.add(line);		    	 
		    	 else 
		    		 {
		    		 result.add("Too long file!!!!!!");
		    		 return result;
		    		 }
		      }
		      proc.waitFor();
		      in.close();
		      out.close();
		      proc.destroy();
		   }
		   catch (Exception e) {
		      e.printStackTrace();
		      return null;
		   }
		}
		return result;
	}
			
		public static void main(String [] args){
			PsWorker worker=new PsWorker();
			ArrayList<String> result=new ArrayList<String>();
			result=worker.executeCommand("lsof -F naLpgt", result);
			for (int i=0;i<result.size();i++){
				System.out.println(result.get(i));
			}			
			ArrayList<String> convResult=StringWorker.convertOutput(result);
			for (int j=0;j<convResult.size();j++){
				System.out.println(convResult.get(j));
			
			}
			System.out.println(convResult.size());
			System.out.println(result.size());

	}
}
