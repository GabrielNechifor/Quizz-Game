create or replace procedure conectare (CONN in out UTL_TCP.CONNECTION) as
begin
    CONN := UTL_TCP.OPEN_CONNECTION(
        REMOTE_HOST   => '127.0.0.1',
        REMOTE_PORT   => 8110,
        TX_TIMEOUT    => 10
    );
end;
/
create or replace procedure comunicare (text in out varchar2,  CONN in out UTL_TCP.CONNECTION, RETVAL in out BINARY_INTEGER, raspuns in out varchar2) as
begin

    RETVAL := UTL_TCP.WRITE_LINE(CONN,text);
    UTL_TCP.FLUSH(CONN);
    
    while UTL_TCP.AVAILABLE(CONN) = 0 loop
    dbms_output.put_line('waiting for something');
    end loop;
    
    BEGIN
        WHILE UTL_TCP.AVAILABLE(CONN) > 0 LOOP
            raspuns := raspuns ||  UTL_TCP.GET_LINE(CONN,TRUE);
        END LOOP;
    EXCEPTION
        WHEN UTL_TCP.END_OF_INPUT THEN
            NULL;
    END;
    text:=raspuns;
end;
/
create or replace procedure deconectare(CONN in out UTL_TCP.CONNECTION) as
begin
    UTL_TCP.CLOSE_CONNECTION(CONN);
end;
/
create or replace procedure client(text in out varchar2,CONN in out UTL_TCP.CONNECTION, RETVAL in out BINARY_INTEGER, raspuns in out varchar2) as
begin
if(text like 'login%') then conectare(CONN); comunicare(text,CONN,RETVAL,raspuns); 
else if(text like 'register%') then conectare(CONN); comunicare(text,CONN,RETVAL,raspuns); deconectare(CONN); 
      else if(text='exit') then deconectare(CONN);
           else comunicare(text,CONN,RETVAL,raspuns); end if;
     end if;
end if;
    
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20101, SQLERRM);
        UTL_TCP.CLOSE_CONNECTION(CONN);
end;
/

declare
--CONN UTL_TCP.CONNECTION;
retval BINARY_INTEGER;
raspuns varchar2(1000):='';
te varchar2(1000);
begin
te:='login';
client(te,tcp_connection.CONN,retval,raspuns);
dbms_output.put_line(te);

te:='please';
client(te,tcp_connection.CONN,retval,raspuns);
dbms_output.put_line(te);
dbms_output.put_line(raspuns);

te:='lucreaza';
client(te,tcp_connection.CONN,retval,raspuns);
dbms_output.put_line(te);

te:='te rog';
client(te,tcp_connection.CONN,retval,raspuns);
dbms_output.put_line(te);

te:='exit';
client(te,tcp_connection.CONN,retval,raspuns);
dbms_output.put_line(te);
end;


create package tcp_connection is
 CONN UTL_TCP.CONNECTION;
 end;