#$CaView = New-Object -ComObject CertificateAuthority.View;
#$CaView | Get-Member;

function Get-CADataBaseSchema {
[CmdletBinding()]
  param(
    [Parameter(Mandatory = $true)]
    [string]$ConfigString,
    [ValidateSet('Request','Extension','Attribute','CRL')]
    [string]$Table = ""
 )
  $CaView = New-Object -ComObject CertificateAuthority.View
  $CaView.OpenConnection($ConfigString)
  switch ($Table) {
    "Request" {$CaView.SetTable(0x0)}
    "Extension" {$CaView.SetTable(0x3000)}
    "Attribute" {$CaView.SetTable(0x4000)}
    "CRL" {$CaView.SetTable(0x5000)}
  }
  $Columns = $CaView.EnumCertViewColumn(0)
  while ($Columns.Next() -ne -1) {
    New-Object psobject -Property @{
      Name = $Columns.GetName()
      DisplayName = $Columns.GetDisplayName()
      Type = switch ($Columns.GetType()) {
        1 {"Long"}
        2 {"DateTime"}
        3 {"Binary"}
        4 {"String"}
      }
      MaxLength = $Columns.GetMaxLength()
    }
  }
}
Get-CADataBaseSchema("ctrl.sbra.com\ctrl");