[System.IO.WatcherChangeTypes]::Created

### SET FOLDER TO WATCH + FILES TO WATCH + SUBFOLDERS YES/NO
    $watcher = New-Object System.IO.FileSystemWatcher
    $watcher.Path = "G:\Tranzit\Filles\other"
    $watcher.Filter = "*.*"
    $watcher.IncludeSubdirectories = $false
    $watcher.EnableRaisingEvents = $true  

### DEFINE ACTIONS AFTER AN EVENT IS DETECTED
    $action = { $path = $Event.SourceEventArgs.FullPath
                $filename = $Event.SourceEventArgs.Name
                $changeType = $Event.SourceEventArgs.ChangeType
                $logline = "$(Get-Date), $changeType, $path"
                Add-content "C:\log.txt" -value $logline
                
				$timeStamp = $Event.TimeGenerated
				
   $text = Get-Content $path -Raw 

   #Write-Host " $text "
   
   [System.Reflection.Assembly]::LoadWithPartialName("System.Data.OracleClient")

   $connectionString = "User Id=AMRA_IMPORT;Password=ver8i;Data Source=ODB;"
   $connection = $null
   $command = $null

   Try
   {
       Write-Host " $changeType "
       $queryString = "insert into SWIFT_FILES_OTHERS (FILENAME,ACTION,FILE_PATH,FILE_B,TIME_CR) VALUES (:FILENAME,:ACTION,:FILE_PATH,:FILE_B,:TIME_CR) "

       $connection = New-Object System.Data.OracleClient.OracleConnection($connectionString)
       $command = New-Object System.Data.OracleClient.OracleCommand -ArgumentList $queryString, $connection  

       $connection.Open()

       $command.Parameters.Add("FILENAME", $filename)
       $command.Parameters.Add("ACTION", "$changeType")
       $command.Parameters.Add("FILE_PATH", $path)
	   $command.Parameters.Add("FILE_B", "$text")
	   $command.Parameters.Add("TIME_CR", "$timeStamp")

       $command.ExecuteNonQuery()
   }
   catch{
   Add-content "C:\log.txt" -value   $Error[0].Exception
   Write-Host " $Error[0].Exception "
    }
   Finally
   {
       if ($connection -ne $null) 
       {
          $connection.Close()
          $connection.Dispose()
       }

       if ($command -ne $null) 
       {
          $command.Dispose()
       }

   }
              }    
### DECIDE WHICH EVENTS SHOULD BE WATCHED 
    Register-ObjectEvent $watcher "Created" -Action $action
    #Register-ObjectEvent $watcher "Changed" -Action $action
    #Register-ObjectEvent $watcher "Deleted" -Action $action
    #Register-ObjectEvent $watcher "Renamed" -Action $action
    while ($true) {sleep 5}