{ Increment program from the BACI Pascal User's Guide }
PROGRAM increment;
CONST
   m = 5;            // the number of times for each thread to increment
VAR
   n : INTEGER;      // the global to be incremented

   /* the threads that increment the global */
   PROCEDURE incr( id : char );
   VAR
      i : INTEGER;
   BEGIN  (* incr *)
      FOR i := 1 TO m DO
      BEGIN
         n := n + 1;
         writeln( id ,'  n =',  n ,'  i = ', i ,' ',id );
      END;
   END;  // of incr 

BEGIN  (* main program *)
   n := 0;
   COBEGIN
      incr( 'A' ); incr( 'B' ); incr( 'C' ); 
   COEND;
   WRITELN( 'The sum is ' , n );
END.
