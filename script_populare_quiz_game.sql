drop table jucatori cascade constraints;
drop table punctaje cascade constraints;
drop table domenii cascade constraints;
drop table intrebari cascade constraints;


create table jucatori (
username varchar2(20) not null,
parola varchar2(20) not null,
primary key(username)
)

create table domenii(
id int not null,
nume_domeniu varchar2(20) not null,
primary key(id)
)

create table punctaje(
id_jucator int not null,
id_domeniu int not null,
punctaj Number(5),
foreign key(id_jucator) references jucatori(id),
foreign key(id_domeniu) references domenii(id),
primary key(id_jucator,id_domeniu)
)

create table intrebari(
id int not null,
intrebare varchar2(1000) not null,
raspuns1 varchar2(500) not null,
raspuns2 varchar2(500) not null,
raspuns3 varchar2(500) not null,
raspuns4 varchar2(500) not null,
corectr1 number(1,0) not null,
corectr2 number(1,0) not null,
corectr3 number(1,0) not null,
corectr4 number(1,0) not null,
id_domeniu number(1,0) not null,
primary key(id),
foreign key(id_domeniu) references domenii(id)
)

declare
v_password varchar2(20);
begin

for v_i in 1..6 loop
   if(dbms_random.value(1,2)<1.5) then 
       v_password:= v_password ||dbms_random.string('A', 1);
       else  v_password:= v_password || to_char(trunc(dbms_random.value(0,9)));
       end if;
       
    end loop;
    dbms_output.put_line(v_password);
end;





SET SERVEROUTPUT ON;
declare
TYPE varr IS VARRAY(1000) OF varchar2(20);
lista_prenume varr := varr('alex','cosmin','alin','david','ion','maria','vasile','laurentiu','bogdan','narcisa','andreea','daniel','florin','pinguinescu','paul','lucian','alexandra','anca','aurelian','catalin');
v_username varchar2(20);
v_username_existent varchar2(20);
v_parola varchar2(20) :='';
v_punctaj int;
v_ok int; 
cursor lista_usernameuri is select username from jucatori;
begin
   
for v_id in 1..1000 loop
     loop
     
     --generare username-uri
     v_username := lista_prenume(TRUNC(DBMS_RANDOM.VALUE(1,lista_prenume.count))) || to_char(TRUNC(dbms_random.value(10000,99999)));
     v_ok:=0;
      open lista_usernameuri;
     loop
         fetch lista_usernameuri into v_username_existent;
         exit when lista_usernameuri%notfound;
         
         if(v_username = v_username_existent) then v_ok:=1;
        end if;
      end loop;
      close lista_usernameuri;
     exit when (v_ok=0);
   
    end loop;
    
    --generare parola
    v_parola:='';
    for v_i in 1..8 loop
   if(dbms_random.value(1,2)<1.5) then 
       v_parola:= v_parola ||dbms_random.string('A', 1);
       else  v_parola:= v_parola || to_char(trunc(dbms_random.value(0,9)));
       end if;
       
    end loop;
    
        dbms_output.put_line('valoarea inserata este: '|| v_username|| ' '|| v_parola);
           insert into jucatori values(v_id,v_username,v_parola);
    end loop;
    
    insert into domenii values(1,'Informatica');
    insert into domenii values(2,'Capitale');

    for v_id in 1..1000 loop
    insert into punctaje values(v_id,1,trunc(dbms_random.value(1,10000)));
    insert into punctaje values(v_id,2,trunc(dbms_random.value(1,10000)));
    end loop;

end;

select id_jucator,punctaj from punctaje order by id_jucator;