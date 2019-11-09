create or replace function z_sb_block return varchar2 is
  db  varchar2(100);
  us  varchar2(100);
  ret varchar2(200);
begin
  begin
  
    select (SELECT host_name FROM v$instance) db, user
      into db, us
      from dual;
    ret := db || '~' || us;
  exception
    when others then
      null;
  end;
  return db || '~' || us;
end z_sb_block;
/
