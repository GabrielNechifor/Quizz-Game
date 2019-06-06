create or replace function rank (v_username varchar2, v_nume_domeniu varchar2)
return int as
  v_rank int;
  v_pct int;
  v_punctaj int;
  v_id_domeniu int;
  begin
    select id into v_id_domeniu from domenii where nume_domeniu=v_nume_domeniu;
    select punctaj into v_pct from (select punctaj,rownum as rh from (select * from punctaje where id_domeniu=v_id_domeniu order by punctaj desc)) where rh=100;
    select punctaj into v_punctaj from punctaje where id_jucator=(select id from jucatori where username=v_username) and id_domeniu=v_id_domeniu;
    if(v_punctaj>=v_pct) then v_rank:=1; end if;
    if(v_punctaj<v_pct and v_punctaj>=((3*v_pct)/4)) then v_rank:=2; end if;
    if(v_punctaj>=(v_pct/2) and v_punctaj<((3*v_pct)/4)) then v_rank:=3; end if;
    if(v_punctaj<(v_pct/2) and v_punctaj>=(v_pct/4)) then v_rank:=4; end if;
    if(v_punctaj<(v_pct/4)) then v_rank:=5; end if;

  return v_rank;
  end;
  
  set serveroutput on;
  begin
  dbms_output.put_line(rank('alex21745',1));
  end;
  
  select punctaj from (select punctaj,rownum as rh from (select * from punctaje where id_domeniu=(select min(id) from domenii where id<2) order by punctaj desc)) where rh=100;
 select * from jucatori;
 
     select punctaj from punctaje where id_jucator=(select id from jucatori where username='alex21745') and id_domeniu=1;

--------------------------------------------------------------------------------------------------------------------------------------

create or replace procedure update_punctaj(v_id_jucator int,v_id_domeniu int,v_punctaj int) as
begin
update punctaje set punctaj=v_punctaj where id_jucator=v_id_jucator and id_domeniu=v_id_domeniu;
end;

set serveroutput on;
declare
p int;
  begin
  update_punctaj(3,1,2345);
  select punctaj into p from punctaje where id_jucator=3 and id_domeniu=1;
  dbms_output.put_line(p);
end;

select * from punctaje where id_jucator=3 and id_domeniu=1;

------------------------------------------------------------------------------------------------------------------------------------------
  create or replace function login(v_username  varchar2, v_password  varchar2)  return int as
exista int:=0;
begin
select count(id) into exista from jucatori where username=v_username and parola=v_password; 
return exista;
end;

select substr('login alex_34 12345',instr('login alex_34 12345',' ',1,1)+1,instr('login alex_34 12345',' ',1,2)-instr('login alex_34 12345',' ',1,1)-1),substr('login alex_34 12345',instr('login alex_34 12345',' ',1,2)+1) from dual; 

 if(login(substr(text,instr(text,' ',1,1)+1,instr(text,' ',1,2)-instr(text,' ',1,1)-1),substr(text,instr(text,' ',1,2)+1))<>0) end if;


----------------------------------------------------------------------------------------------------------------------------------------------------
create or replace procedure insert_camera(v_username in varchar2,v_id out number) as

contor number :=0;
v_i number;
v_aux number:=0;
begin
v_id :=-1;
select count(*) into contor from camere; 

for v_i in 1..contor loop
  select count(*) into v_aux from camere where id=v_i;
  if( v_aux =0 ) then v_id:=v_i; exit; end if;
end loop;

if (v_id=-1) then v_id:=contor+1; end if;

insert into camere values(v_id,v_username,null,-1,-1);

end;

------------------------------------------------------------------------------------------------------------------------

create or replace procedure insert_intrebari(v_id_camera in number,v_nume_domeniu in varchar2) as
v_i number :=1;
v_id_intrebare number;
v_contor number :=0;
v_nr number;
v_ok number;
v_id_domeniu number;
begin
select id into v_id_domeniu from domenii where nume_domeniu=v_nume_domeniu;
select count(*) into v_nr from intrebari;

while(v_i<=10) loop 

v_ok:=0;
while(v_ok<>1) loop
v_id_intrebare := round(dbms_random.value(1,v_nr));
select count(*) into v_contor from intrebari where id=v_id_intrebare and id_domeniu=v_id_domeniu;
if(v_contor<> 0) then v_ok:=1;
end if;
end loop;

select count(*) into v_contor from intr_camera where id_camera=v_id_camera and id_intr=v_id_intrebare;
if(v_contor= 0) then 
      insert into intr_camera values (v_id_camera,v_id_intrebare,0);
           v_i:= v_i+1;
end if;

end loop;
end;

-------------------------------------------------------------------------------------------------------------------------------------------------

create or replace procedure update_camera(v_id_camera in number ,v_username in varchar2,v_mesaj out varchar2) as

v_contor number :=0;
v_i number;
v_aux number:=0;
begin

select count(*) into v_contor from camere where id=v_id_camera; 
if(v_contor = 0) then v_mesaj := 'Camera aleasa nu exista'; end if;

select count(*) into v_contor from camere where id=v_id_camera and second_player is not null; 
if(v_contor <> 0) then v_mesaj := 'Camera aleasa este ocupata deja';  
else
update camere set second_player=v_username where id=v_id_camera;
if (v_mesaj is null) then v_mesaj:= 'Daca esti pregatit de joc scrie start'; end if;
end if;
end;

------------------------------------------------------------------------------------------------------------
-- selecteaza o intrebare din camera data si apoi o sterge
create or replace procedure select_o_intrebare(v_id_camera in int, v_id_intr out int) as
begin
select id_intr into v_id_intr from (select * from intr_camera where id_camera=v_id_camera) where rownum<2;
end;
-------------------------------------------------------------------------------------------------
---jucatorul raspunde la intrebare
create or replace procedure raspunde_la_intrebare(v_id_camera in int, v_id_intr in int) as
begin
update intr_camera set nr_rasp = nr_rasp + 1 where id_camera=v_id_camera and id_intr=v_id_intr;
end;
-------------------------------------------------------------------------------------------------------------
------ sterge intrebarea folosita
create or replace procedure sterge_o_intrebare(v_id_camera in int, v_id_intr in int) as
v_count int;
begin
select count(*) into v_count from intr_camera where id_camera=v_id_camera and id_intr=v_id_intr;
if(v_count>0) then
 delete from intr_camera where id_intr=v_id_intr and id_camera=v_id_camera;
end if;
end;
-------------------------------------------------------------------------------------------------
---verificam daca au raspuns ambii jucatori la intrebare
create or replace function au_raspuns_ambii(v_id_camera in int, v_id_intr in int)
return int as
v_nr_rasp int:=0;
statut int:=0;
v_count int;
begin
select count(nr_rasp) into v_count from intr_camera where id_camera=v_id_camera and id_intr=v_id_intr;
if(v_count>0) then
select nr_rasp into v_nr_rasp from intr_camera where id_camera=v_id_camera and id_intr=v_id_intr;
end if;
if (v_nr_rasp = 2 or v_count=0) then statut:=1; end if;
return statut;
end;
-------------------------------------------------------------------------------------------------------------
-- verifica daca ambii jucatori au intrat in camera
create or replace function verif_juc(v_id_camera in int)
return int as
ok int:=0;
v_first_player varchar2(100);
v_second_player varchar2(100);
begin
select first_player,second_player into v_first_player,v_second_player from camere where id=v_id_camera;
if(v_first_player is not null and v_second_player is not null) then ok:=1; end if;
return ok;
end;
------------------------------------------------------------------------------------------------------------
-----Arata castigatorulz
create or replace function mesaj_final(v_id_camera number,v_username varchar2,v_id_domeniu int) return varchar2 as
v_punctaj1 number:=-1;
v_punctaj2 number:=-1;
v_jucator1 varchar2(50);
v_jucator2 varchar2(50);
v_mesaj varchar2(50):='';
v_id_jucator1 int;
v_id_jucator2 int;
begin

select first_player,second_player,first_pl_pct,second_pl_pct into v_jucator1,v_jucator2,v_punctaj1,v_punctaj2 from camere where id=v_id_camera;

select id into v_id_jucator1 from jucatori where username=v_jucator1;
select id into v_id_jucator2 from jucatori where username=v_jucator2;

if(v_punctaj1=v_punctaj2) then v_mesaj:='Egalitate'; update_punctaj(v_id_jucator1,v_id_domeniu,v_punctaj1); update_punctaj(v_id_jucator2,v_id_domeniu,v_punctaj2); end if;
if(v_punctaj1>v_punctaj2 and v_jucator1=v_username)  then v_mesaj:='Yuuuupiiii! Ai castigat!'; update_punctaj(v_id_jucator1,v_id_domeniu,v_punctaj1);  end if;
if(v_punctaj1>v_punctaj2 and v_jucator2=v_username)  then v_mesaj:='Oh nu! Ai pierdut...'; end if;
if(v_punctaj1<v_punctaj2 and v_jucator1=v_username)  then v_mesaj:='Oh nu! Ai pierdut...'; end if;
if(v_punctaj1<v_punctaj2 and v_jucator2=v_username)  then v_mesaj:='Yuuuupiiii! Ai castigat!'; update_punctaj(v_id_jucator2,v_id_domeniu,v_punctaj2); end if;

if(v_mesaj is null) then v_mesaj:='Nume de utilizator invalid sau camera inexistenta'; end if;

return v_mesaj;
end;
-------------------------------------------------------------------------------------------------------------------------------
---Punctaj pe o intrebare
create or replace function punctaj_intrebare(v_id_intrebare int,r_1 int,r_2 int,r_3 int,r_4 int)
  return int as
  v_punctaj int;
  v_1 int;
  v_2 int;
  v_3 int;
  v_4 int;
  nr_r_corecte int;
  nr_v_corecte int;
  begin
  nr_r_corecte:=0;
   nr_v_corecte:=0;
  select corectr1,corectr2,corectr3,corectr4 into v_1,v_2,v_3,v_4 from intrebari where id=v_id_intrebare;
 if(v_1=1) then nr_v_corecte:=nr_v_corecte+1; end if;
  if(v_2=1) then nr_v_corecte:=nr_v_corecte+1; end if;
  if(v_3=1) then nr_v_corecte:=nr_v_corecte+1; end if;
  if(v_4=1) then nr_v_corecte:=nr_v_corecte+1; end if;
  if(r_1=v_1 and r_2=v_2 and r_3=v_3 and r_4=v_4) then v_punctaj:=1000;
    else if(r_1<>v_1 and r_2<>v_2 and r_3<>v_3 and r_4<>v_4) then v_punctaj:=0;
       else 
          if(r_1=v_1 and r_1=1) then nr_r_corecte:=nr_r_corecte+1; end if;
          if(r_1<>v_1) then nr_r_corecte:=nr_r_corecte-1; end if;
          if(r_2=v_2 and r_2=1) then nr_r_corecte:=nr_r_corecte+1; end if;
          if(r_2<>v_2) then nr_r_corecte:=nr_r_corecte-1; end if;
          if(r_3=v_3 and r_3=1) then nr_r_corecte:=nr_r_corecte+1; end if;
          if(r_3<>v_3) then nr_r_corecte:=nr_r_corecte-1; end if;
          if(r_4=v_4 and r_4=1) then nr_r_corecte:=nr_r_corecte+1; end if;
          if(r_4<>v_4) then nr_r_corecte:=nr_r_corecte-1; end if;
                 dbms_output.put_line('a intrat aici cu: '||nr_v_corecte||' '||nr_r_corecte);
          if(nr_r_corecte<=0) then v_punctaj:=0;
           else
          v_punctaj:=trunc((nr_r_corecte/nr_v_corecte)*1000);
          end if;
        -- if(v_punctaj=1000) then v_punctaj:=0; end if;
      end if;
    
  end if; 
  return v_punctaj;
  end;
  --------------------------------------------------------------------------------------------------
  ----adaugam punctajul final pentru o runda in camera
create or replace procedure add_pct (v_id_camera int,v_punctaj int, statut int) as
begin
if(statut = 1) then update camere set first_pl_pct=v_punctaj where id=v_id_camera;
               else update camere set second_pl_pct=v_punctaj where id=v_id_camera;
end if;
end;
--------------------------------------------------------------------------------------
----verificam daca in camera sunt puse punctajele ambilor jucatori
create or replace function verif_pct (v_id_camera int)
return int as
situatia int:=1;
punctaj1 int;
punctaj2 int;
begin
select first_pl_pct,second_pl_pct into punctaj1,punctaj2 from camere where id=v_id_camera;
if(punctaj1=-1 or punctaj2=-1) then situatia:=0; end if;
return situatia;
end;
------------------------------------------------------------------------------------------------------
----inregistrarea unui jucator
create or replace function user_register(v_username varchar2, v_password varchar2)
return varchar2 as
mesaj varchar2(100);
exista int;
begin
if(substr(v_username,1,1)<'A' or substr(v_username,1,1)>'z') then mesaj:='Username invalid';
else if(length(v_username)<5) then mesaj:='Username-ul introdus este prea mic'; end if;
     if(length(v_username)>15) then mesaj:='Username-ul introdus este prea mare'; end if;
     select count(username) into exista from jucatori where username=v_username;
     if(exista=1) then mesaj:='Username nedisponibil';
     else if(length(v_password)<4) then mesaj:='Parola introdusa este prea mica'; 
          else if(length(v_password)>15) then mesaj:='Parola introdusa este prea mare';
               else insert into jucatori values((select max(id) from jucatori)+1,v_username,v_password);
                    insert into punctaje values((select id from jucatori where username=v_username),1,0);
                    insert into punctaje values((select id from jucatori where username=v_username),2,0);
                    mesaj:='Inregistrarea a decurs cu succes';
              end if;
          end if;
    end if;
end if;
 return mesaj;
end;