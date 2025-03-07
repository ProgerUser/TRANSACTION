$CaView = New-Object -ComObject CertificateAuthority.View
$CaView.OpenConnection("ctrl.sbra.com\ctrl")

$properties = 
"CertificateTemplate",
"RequestID",
"RequesterName",
"NotBefore",
"NotAfter",
"Disposition",
"UPN",
"Request.DispositionMessage",
"Request.SubmittedWhen",
"Request.ResolvedWhen"

$CaView.SetResultColumnCount($properties.Count)
$properties | %{$CAView.SetResultColumn($CAView.GetColumnIndex($False, $_))}

#$NewObject02=$null
$NewObject02=@()


$operator = @{"eq" = 1;"le" = 2; "lt" = 4; "ge" = 8; "gt" = 16}
# получаем номер столбца, по которому будет производиться фильтрация
$RColumn = $CAView.GetColumnIndex($False, "CertificateTemplate")
# устанавливаем сам фильтр
$CaView.SetRestriction($RColumn,$operator["le"],0,"Machine")


# получаем номер столбца, по которому будет производиться фильтрация
$RColumn = $CAView.GetColumnIndex($False, "Disposition")
# устанавливаем сам фильтр
$CaView.SetRestriction($RColumn,1,0,20)


$RColumn = $CAView.GetColumnIndex($False, "NotAfter")
$CaView.SetRestriction($RColumn,0x10,1,[datetime]::Now)

$Row = $CaView.OpenView()
$cnt = 0
# начинаем шахматы и шагать шаговым искателем по строчкам
while ($Row.Next() -ne -1) {
    # создаём пустой объект, который будет являться нашим выходным объектом
    $cert = New-Object psobject
    # переставляем шаговый искатель с вертикального в горизонтальное направление
    $Column = $Row.EnumCertViewColumn()
    # шагаем последовательно по столбцам, которые мы определили вначале скрипта
    while ($Column.Next() -ne -1) {
    
    #if($Column.GetName() -eq "CertificateTemplate" -and $Column.GetValue(1) -ne "Machine" ) {
        $cnt += 1
        # извлекаем название текущего столбца
        $current = $Column.GetName()
        # добавляем свойство к нашему выходному объекту с именем равным имени столбца
        # и добавляем соответствующее значение столбца текущей строчки
        $Cert | Add-Member -MemberType NoteProperty $($Column.GetName()) -Value $($Column.GetValue(1)) -Force
    
        $NewObject02 += $Cert#[string]$Column.GetValue(1)#$Cert 
       # }
    }
    # выкидываем объект на выход
    $Cert
    # сбрасываем положение горизонтального шагового искателя
    $Column.Reset()
}
# сбрасываем положение вертикального шагового искателя
$Row.Reset()

$CERTS_SHEDULER = [System.Environment]::GetEnvironmentVariable('CERTS_SHEDULER','machine') + "\processes.csv"

$NewObject02 | export-csv $CERTS_SHEDULER -delimiter '~' -encoding utf8