 #The dataLakeStoreName and dataLakeAnalyticsName shoud contain only
 #lower case letters and numbers.
 
 $resourceGroupName = "VCVResourceGroup"
 $dataLakeStoreName = "vcvdatalake"
 $dataLakeAnalyticsName = "vcvdatalakeanalytics"
 $location = "East US 2"

 Write-Host "Create a resource group ..." -ForegroundColor Green
 New-AzureRmResourceGroup `
     -Name  $resourceGroupName `
     -Location $location

 Write-Host "Create a Data Lake account ..."  -ForegroundColor Green
 New-AzureRmDataLakeStoreAccount `
     -ResourceGroupName $resourceGroupName `
     -Name $dataLakeStoreName `
     -Location $location

 Write-Host "Create a Data Lake Analytics account ..."  -ForegroundColor Green
 New-AzureRmDataLakeAnalyticsAccount `
     -Name $dataLakeAnalyticsName `
     -ResourceGroupName $resourceGroupName `
     -Location $location `
     -DefaultDataLake $dataLakeStoreName

 Write-Host "The newly created Data Lake Analytics account ..."  -ForegroundColor Green
 Get-AzureRmDataLakeAnalyticsAccount `
     -ResourceGroupName $resourceGroupName `
     -Name $dataLakeAnalyticsName  

     