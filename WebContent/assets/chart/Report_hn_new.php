<?php
session_start();
require '../connect/connect.php';
function ShowHN($conn, $DATA)
{
  $count = 0;
  // =================================================================
    $Sql = "SELECT
                      hotpitalnumber.HnCode, 
                      hotpitalnumber.Id ,
                      hotpitalnumber.FName

                    FROM
                      hotpitalnumber ";
    $meQuery = mysqli_query($conn, $Sql);
    while ($Result = mysqli_fetch_assoc($meQuery)) 
    {
      $return[$count]['HnCode']   = $Result['HnCode'];
      $return[$count]['Id']   = $Result['Id'];
      $return[$count]['FName']   = $Result['FName'];
      $count++;
    }

    $return['count'] = $count;
  // =================================================================

  if ($count > 0)
  {
    $return['status'] = "success";
    $return['form'] = "ShowHN";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  } 
  else 
  {
    $return['status'] = "success";
    $return['form'] = "ShowHN";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  }

}
function GetHN($conn, $DATA)
{
  $count = 0;
  $hncode = $DATA["hncode"];
  $hnname = $DATA["hnname"];
  $chk = $DATA["chk"];
  if($chk ==1)
  {
    $id = $hncode;
  }
  else
  {
    $id = $hnname;
  }
  
  $Sql = "SELECT
            hotpitalnumber.HnCode, 
            hotpitalnumber.Id ,
            hotpitalnumber.FName ,
            hotpitalnumber.HnAge
          FROM
            hotpitalnumber
          WHERE hotpitalnumber.Id = '$id'  ";
          $meQuery = mysqli_query($conn, $Sql);
          while ($Result = mysqli_fetch_assoc($meQuery)) 
          {
            $return['HnCode']   = $Result['HnCode'];
            $return['Id']   = $Result['Id'];
            $return['FName']   = $Result['FName'];
            $return['HnAge']   = $Result['HnAge'];
            $count++;
          }
    $return['count'] = $count;
  // =================================================================

  if ($count > 0)
  {
    $return['status'] = "success";
    $return['form'] = "GetHN";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  } 
  else 
  {
    $return['status'] = "success";
    $return['form'] = "GetHN";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  }

}
function GetHN_create($conn, $DATA)
{
  $count = 0;
  $hncode = $DATA["hncode"];
  $hnname = $DATA["hnname"];
  $chk = $DATA["chk"];
  if($chk ==1)
  {
    $id = $hncode;
  }
  else
  {
    $id = $hnname;
  }
  
  $Sql = "SELECT
            hotpitalnumber.HnCode, 
            hotpitalnumber.Id ,
            hotpitalnumber.FName ,
            hotpitalnumber.HnAge
          FROM
            hotpitalnumber
          WHERE hotpitalnumber.Id = '$id'  ";
          $meQuery = mysqli_query($conn, $Sql);
          while ($Result = mysqli_fetch_assoc($meQuery)) 
          {
            $return['HnCode']   = $Result['HnCode'];
            $return['Id']   = $Result['Id'];
            $return['FName']   = $Result['FName'];
            $return['HnAge']   = $Result['HnAge'];
            $count++;
          }
    $return['count'] = $count;
  // =================================================================

  if ($count > 0)
  {
    $return['status'] = "success";
    $return['form'] = "GetHN_create";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  } 
  else 
  {
    $return['status'] = "success";
    $return['form'] = "GetHN_create";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  }

}
function ShowDetailHN($conn, $DATA)
{
  $count = 0;
  $hncode = $DATA["hncode"];
  $hnname = $DATA["hnname"];
  $sDate = $DATA["sDate"];
  $eDate = $DATA["eDate"];


  $sDatex = explode("/",$sDate);
  $sDatex = $sDatex[2]."-".$sDatex[1]."-".$sDatex[0];

  $eDatex = explode("/",$eDate);
  $eDatex = $eDatex[2]."-".$eDatex[1]."-".$eDatex[0];

  $Deptid = $_SESSION["Deptid"];
 
  
  $Sql = "SELECT
            hotpitalnumber.HnCode,
            hotpitalnumber.FName,
            itemstock.UsageCode,
            item.itemname,
            DATE_FORMAT( hncode.DocDate, '%d/%m/%Y' ) AS DocDate,
            hotpitalnumber.HnAge,
            hotpitalnumber.HnMonth,
            PERIOD_DIFF(
              DATE_FORMAT( NOW(), '%Y%m' ),
            DATE_FORMAT( hotpitalnumber.CreateDate, '%Y%m' )) AS DiffMonth 
          FROM
            hncode
            INNER JOIN hncode_detail ON hncode.DocNo = hncode_detail.DocNo
            INNER JOIN itemstock ON hncode_detail.ItemStockID = itemstock.RowID
            INNER JOIN item ON itemstock.ItemCode = item.itemcode
            INNER JOIN hotpitalnumber ON hncode.HnCode = hotpitalnumber.HnCode 
          WHERE
            hotpitalnumber.Id = $hncode 
            AND hncode.IsCancel = 0 
            AND hncode.DeptID = '$Deptid' 
            AND DocDate BETWEEN '$sDatex' AND '$eDatex' 
          ORDER BY
            hncode.DocDate DESC  ";

          $meQuery = mysqli_query($conn, $Sql);
          while ($Result = mysqli_fetch_assoc($meQuery)) 
          {
            $return[$count]['HnCode'] = $Result['HnCode'];
            $return[$count]['FName'] = $Result['FName'];
            $return[$count]['DocDate'] = $Result['DocDate'];
            $return[$count]['UsageCode'] = $Result['UsageCode'];
            $return[$count]['itemname'] = $Result['itemname'];
            
            $DiffMonth = $Result['DiffMonth'];
            $month = $Result['HnMonth'] + $DiffMonth;
            $age = $Result['HnAge'] + floor($month / 12);

            $return[$count]['HnAge'] = $age;
            $return[$count]['HnMonth'] = ($month % 12);
            $count++;
          }
    $return['count'] = $count;
  // =================================================================

  if ($count > 0)
  {
    $return['status'] = "success";
    $return['form'] = "ShowDetailHN";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  } 
  else 
  {
    $return['status'] = "success";
    $return['form'] = "ShowDetailHN";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  }

}
function gototrace($conn, $DATA)
{
  $count = 0;
  $usagecode = $DATA["usagecode"];

  $Sql = "SELECT
            DATE_FORMAT( NOW(), '%d-%m-%Y' ) AS ddate,
            item.itemname,
            itemstock.UsageCode,
            DATE_FORMAT( wash.DocDate, '%d-%m-%Y' ) AS washdate,
            washmachine.MachineName AS WashMachineName,
            wash.WashRoundNumber AS WashRoundNumber,
            TIME( wash.StartTime ) AS timeSdatew,
            TIME( wash.FinishTime ) AS timeEdatew,
            CONCAT( emp1.FirstName, ' ', emp1.LastName ) AS washBeforeApprovename,
            CONCAT( emp2.FirstName, ' ', emp2.LastName ) AS washAfterApprovename,
            DATE_FORMAT( sterile.DocDate, '%d-%m-%Y' ) AS steriledate,
            sterilemachine.MachineName2 AS SterileMachineName,
            sterile.SterileRoundNumber AS SterileRoundNumber,
            TIME( sterile.StartTime ) AS timeSdates,
            TIME( sterile.FinishTime ) AS timeEdates,
            CONCAT( emp3.FirstName, ' ', emp3.LastName ) AS ppsname,
            CONCAT( emp4.FirstName, ' ', emp4.LastName ) AS appsname,
            CONCAT( emp5.FirstName, ' ', emp5.LastName ) AS sterilesname,
            CONCAT( emp6.FirstName, ' ', emp6.LastName ) AS sterileBeforeApprovename,
            CONCAT( emp7.FirstName, ' ', emp7.LastName ) AS sterileAfterApprovename 
          FROM
            itemstock
            LEFT JOIN item ON itemstock.ItemCode = item.itemcode
            LEFT JOIN washdetail ON itemstock.RowID = washdetail.ItemStockID
            LEFT JOIN wash ON washdetail.WashDocNo = wash.DocNo
            LEFT JOIN washmachine ON wash.WashMachineID = washmachine.ID
            LEFT JOIN employee AS emp1 ON wash.BeforeApprove = emp1.EmpCode
            LEFT JOIN employee AS emp2 ON wash.AfterApprove = emp2.EmpCode
            LEFT JOIN steriledetail ON itemstock.RowID = steriledetail.ItemStockID
            LEFT JOIN sterile ON steriledetail.DocNo = sterile.DocNo
            LEFT JOIN sterilemachine ON sterile.SterileMachineID = sterilemachine.ID
            LEFT JOIN employee AS emp3 ON sterile.PrepareCode = emp3.ID
            LEFT JOIN employee AS emp4 ON sterile.ApproveCode = emp4.ID
            LEFT JOIN employee AS emp5 ON sterile.SterileCode = emp5.ID
            LEFT JOIN employee AS emp6 ON sterile.BeforeApprove = emp6.EmpCode
            LEFT JOIN employee AS emp7 ON sterile.AfterApprove = emp7.EmpCode 
          WHERE
            itemstock.UsageCode = '$usagecode' 
          ORDER BY
            wash.DocDate DESC,
            sterile.DocDate DESC 
            LIMIT 1 ";
          $meQuery = mysqli_query($conn, $Sql);
          while ($Result = mysqli_fetch_assoc($meQuery)) 
          {
            $return['ddate'] = $Result['ddate'];
            $return['itemname'] = $Result['itemname'];
            $return['UsageCode'] = $Result['UsageCode'];
            $return['timeSdates'] = $Result['timeSdates']." น.";
            $return['timeEdates'] = $Result['timeEdates']." น.";
            $return['steriledate'] = $Result['steriledate'];
            $return['SterileMachineName'] = $Result['SterileMachineName'];
            $return['SterileRoundNumber'] = $Result['SterileRoundNumber'];
            $return['ppsname'] = $Result['ppsname'];
            $return['appsname'] = $Result['appsname'];
            $return['sterilesname'] = $Result['sterilesname'];
            $return['sterileBeforeApprovename'] = $Result['sterileBeforeApprovename'];
            $return['sterileAfterApprovename'] = $Result['sterileAfterApprovename'];
            $return['washBeforeApprovename'] = $Result['washBeforeApprovename'];
            $return['washAfterApprovename'] = $Result['washAfterApprovename'];
            $return['washdate'] = $Result['washdate'];
            $return['WashMachineName'] = $Result['WashMachineName'];
            $return['WashRoundNumber'] = $Result['WashRoundNumber'];
            $return['timeSdatew'] = $Result['timeSdatew']." น.";
            $return['timeEdatew'] = $Result['timeEdatew']." น.";
            $count++;
          }
    $return['count'] = $count;
  // =================================================================

  if ($count > 0)
  {
    $return['status'] = "success";
    $return['form'] = "gototrace";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  } 
  else 
  {
    $return['status'] = "success";
    $return['form'] = "gototrace";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  }

}
function SearchDocHN($conn, $DATA)
{
  $count = 0;
  $hncode = $DATA["hncode_search"];
  $sDate = $DATA["sDate_search"];
  $eDate = $DATA["eDate_search"];


  $sDatex = explode("/",$sDate);
  $sDatex = $sDatex[2]."-".$sDatex[1]."-".$sDatex[0];

  $eDatex = explode("/",$eDate);
  $eDatex = $eDatex[2]."-".$eDatex[1]."-".$eDatex[0];

  $Deptid = $_SESSION["Deptid"];
  
  if($hncode <> '-')
  {
    $show_hn = "AND hotpitalnumber.Id = '$hncode' ";
  }
  else
  {
    $show_hn = " ";
  }
  $Sql = "SELECT
            hncode.DocNo,
            DATE_FORMAT( hncode.DocDate, '%d/%m/%Y' ) AS DocDate ,
            hotpitalnumber.FName
          FROM
            hncode
            INNER JOIN hncode_detail ON hncode.DocNo = hncode_detail.DocNo
            INNER JOIN itemstock ON hncode_detail.ItemStockID = itemstock.RowID
            INNER JOIN item ON itemstock.ItemCode = item.itemcode
            INNER JOIN hotpitalnumber ON hncode.HnCode = hotpitalnumber.HnCode 
          WHERE
             hncode.IsCancel = 0 
            AND hncode.DeptID = '$Deptid'
            $show_hn
            AND DocDate BETWEEN '$sDatex' AND '$eDatex' 
          GROUP BY hncode.DocNo
          ORDER BY
            hncode.DocDate DESC  ";
          $meQuery = mysqli_query($conn, $Sql);
          while ($Result = mysqli_fetch_assoc($meQuery)) 
          {
            $return[$count]['DocNo'] = $Result['DocNo'];
            $return[$count]['FName'] = $Result['FName'];
            $return[$count]['DocDate'] = $Result['DocDate'];
            $count++;
          }
    $return['count'] = $count;
  // =================================================================

  if ($count > 0)
  {
    $return['status'] = "success";
    $return['form'] = "SearchDocHN";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  } 
  else 
  {
    $return['status'] = "success";
    $return['form'] = "SearchDocHN";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  }

}
function gotohome($conn, $DATA)
{
  $count = 0;
  $docno = $DATA["docno"];
  $Deptid = $_SESSION["Deptid"];
  

  $Sql = "SELECT
            hotpitalnumber.Id,
            hotpitalnumber.HnCode,
            hotpitalnumber.FName,
            hotpitalnumber.HnAge,
            DATE_FORMAT( hncode.DocDate, '%d/%m/%Y' ) AS DocDate,
            itemstock.UsageCode,
            item.itemname 
          FROM
            hncode
            INNER JOIN hncode_detail ON hncode.DocNo = hncode_detail.DocNo
            INNER JOIN itemstock ON hncode_detail.ItemStockID = itemstock.RowID
            INNER JOIN item ON itemstock.ItemCode = item.itemcode
            INNER JOIN hotpitalnumber ON hncode.HnCode = hotpitalnumber.HnCode 
          WHERE
            hncode.IsCancel = 0 
            AND hncode.DocNo = '$docno' 
            AND hncode.DeptID = '$Deptid' 
          ORDER BY
            hncode.DocDate DESC  ";
          $meQuery = mysqli_query($conn, $Sql);
          while ($Result = mysqli_fetch_assoc($meQuery)) 
          {
            $return[$count]['Id'] = $Result['Id'];
            $return[$count]['HnCode'] = $Result['HnCode'];
            $return[$count]['FName'] = $Result['FName'];
            $return[$count]['HnAge'] = $Result['HnAge'];
            $return[$count]['DocDate'] = $Result['DocDate'];
            $return[$count]['UsageCode'] = $Result['UsageCode'];
            $return[$count]['itemname'] = $Result['itemname'];
            $count++;
          }
    $return['count'] = $count;
  // =================================================================

  if ($count > 0)
  {
    $return['status'] = "success";
    $return['form'] = "gotohome";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  } 
  else 
  {
    $return['status'] = "success";
    $return['form'] = "gotohome";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  }

}
function CreateDocument($conn, $DATA)
{
  $count    = 0;
  $hncode   = $DATA["hncode"];
  $hnname   = $DATA["hnname"];
  $hnaage   = $DATA["hnaage"];
  $hntitle   = $DATA["hntitle"];
  $check_hn = $DATA["check_hn"];
  $Deptid   = $_SESSION["Deptid"];
  $Userid = $_SESSION['Userid'];
  $date = date('d/m/Y');
  // 0 รายเก่า / 1 รายใหม่
  if($check_hn == '0')
  {
    $SqlS =  "SELECT CONCAT('HN',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'-', LPAD( (COALESCE(MAX(CONVERT(SUBSTRING(DocNo,8,5),UNSIGNED INTEGER)),0)+1) ,5,0)) AS DocNo
        FROM hncode
        WHERE DocNo Like CONCAT('HN',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'%')
        ORDER BY DocNo DESC LIMIT 1";
        $meQuerys = mysqli_query($conn, $SqlS);
        while ($ResultS = mysqli_fetch_assoc($meQuerys))
        {
          $docno_hn = $ResultS["DocNo"];
          $Sql_insert_hn = "INSERT INTO hncode(
            hncode.DocNo,
            hncode.DocDate,
            hncode.HnCode,
            hncode.ModifyDate,
            hncode.UserCode,
            hncode.DeptID,
            hncode.Qty,
            hncode.DocNo_SS,
            hncode.IsStatus,
            hncode.Remark,
            hncode.IsCancel,
            hncode.B_ID
          )VALUES(
            '$docno_hn',
            DATE(NOW()),
            '$hncode',
            NOW(),
            '$Userid',
            '$Deptid',
            0,
            '',
            0,
            '',
            0,
            '1'
          )";
          mysqli_query($conn, $Sql_insert_hn);
        }
  }
  else
  {
    $Sql_insert = "INSERT INTO hotpitalnumber(
      hotpitalnumber.HnCode,
      hotpitalnumber.TitleName,
      hotpitalnumber.FName,
      hotpitalnumber.HnAge,
      hotpitalnumber.CreateDate
      )
      VALUES
      (
      '$hncode',
      '$hntitle',
      '$hnname',
      '$hnaage',
      NOW() )";    
      if(mysqli_query($conn, $Sql_insert))
      {
        $SqlS =  "SELECT CONCAT('HN',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'-', LPAD( (COALESCE(MAX(CONVERT(SUBSTRING(DocNo,8,5),UNSIGNED INTEGER)),0)+1) ,5,0)) AS DocNo
        FROM hncode
        WHERE DocNo Like CONCAT('HN',SUBSTRING(YEAR(DATE(NOW())),3,4),LPAD(MONTH(DATE(NOW())),2,0),'%')
        ORDER BY DocNo DESC LIMIT 1";
        $meQuerys = mysqli_query($conn, $SqlS);
        while ($ResultS = mysqli_fetch_assoc($meQuerys))
        {
          $docno_hn = $ResultS["DocNo"];
          $Sql_insert_hn = "INSERT INTO hncode(
            hncode.DocNo,
            hncode.DocDate,
            hncode.HnCode,
            hncode.ModifyDate,
            hncode.UserCode,
            hncode.DeptID,
            hncode.Qty,
            hncode.DocNo_SS,
            hncode.IsStatus,
            hncode.Remark,
            hncode.IsCancel,
            hncode.B_ID
          )VALUES(
            '$docno_hn',
            DATE(NOW()),
            '$hncode',
            NOW(),
            '$Userid',
            '$Deptid',
            0,
            '',
            0,
            '',
            0,
            '1'
          )";
          mysqli_query($conn, $Sql_insert_hn);
        }
      }
  }





  $return['count'] = $count;
  $return['date'] = $date;
  $return['docno_hn'] = $docno_hn;
  
  // =================================================================


    $return['status'] = "success";
    $return['form'] = "CreateDocument";
    echo json_encode($return);
    mysqli_close($conn);
    die;
}
function checkstock($conn, $DATA)
{
  $count    = 0;
  $usagecode   = $DATA["usagecode"];
  $DocNo_HN   = $DATA["DocNo_HN"];


  $Sql = "SELECT
            itemstock.RowID
          FROM
            itemstock
          WHERE IsStatus = '5' AND IsPay = '1' AND UsageCode = '$usagecode' ";
          $meQuery = mysqli_query($conn, $Sql);
          while ($Result = mysqli_fetch_assoc($meQuery))
          {
            $RowID = $Result["RowID"];
            $sql_insert = "INSERT INTO hncode_detail SET DocNo = '$DocNo_HN' , ItemStockID = '$RowID' , Qty = '1' , IsStatus = '1' , IsCancel = '0' , B_ID = '1' ";
            mysqli_query($conn, $sql_insert);
            $count ++;
          }



  $return['count'] = $count;
  // =================================================================


    $return['status'] = "success";
    $return['form'] = "checkstock";
    echo json_encode($return);
    mysqli_close($conn);
    die;
}
function ShowDetailHN_right($conn, $DATA)
{
  $count = 0;
  $DocNo_HN = $DATA["DocNo_HN"];
 
  
  $Sql = "  SELECT
              item.itemname,
              hncode_detail.ID,
              itemstock.UsageCode 
            FROM
              hncode_detail
              INNER JOIN itemstock ON hncode_detail.ItemStockID = itemstock.RowID
              INNER JOIN item ON itemstock.ItemCode = item.itemcode
            WHERE hncode_detail.DocNo = '$DocNo_HN' ";

          $meQuery = mysqli_query($conn, $Sql);
          while ($Result = mysqli_fetch_assoc($meQuery)) 
          {
            $return[$count]['itemname'] = $Result['itemname'];
            $return[$count]['ID'] = $Result['ID'];
            $return[$count]['UsageCode'] = $Result['UsageCode'];
            $count++;
          }
    $return['count'] = $count;
  // =================================================================

  if ($count > 0)
  {
    $return['status'] = "success";
    $return['form'] = "ShowDetailHN_right";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  } 
  else 
  {
    $return['status'] = "success";
    $return['form'] = "ShowDetailHN_right";
    echo json_encode($return);
    mysqli_close($conn);
    die;
  }

}

function delitem($conn, $DATA)
{
  $Id = $DATA["Id"];
  $DocNo_HN = $DATA["DocNo_HN"];
  
  $Sql = "DELETE FROM hncode_detail WHERE ID = '$Id'    ";
  mysqli_query($conn, $Sql);


  ShowDetailHN_right($conn, $DATA);
}


//==========================================================
//
//==========================================================
if (isset($_POST['DATA']))
{
  $data = $_POST['DATA'];
  $DATA = json_decode(str_replace('\"', '"', $data), true);

  if ($DATA['STATUS'] == 'ShowHN')
  {
    ShowHN($conn, $DATA);
  }
  else if ($DATA['STATUS'] == 'GetHN')
  {
    GetHN($conn, $DATA);
  }
  else if ($DATA['STATUS'] == 'ShowDetailHN')
  {
    ShowDetailHN($conn, $DATA);
  }
  else if ($DATA['STATUS'] == 'gototrace')
  {
    gototrace($conn, $DATA);
  }
  else if ($DATA['STATUS'] == 'SearchDocHN')
  {
    SearchDocHN($conn, $DATA);
  }
  else if ($DATA['STATUS'] == 'gotohome')
  {
    gotohome($conn, $DATA);
  }
  else if ($DATA['STATUS'] == 'GetHN_create')
  {
    GetHN_create($conn, $DATA);
  }
  else if ($DATA['STATUS'] == 'CreateDocument')
  {
    CreateDocument($conn, $DATA);
  }
  else if ($DATA['STATUS'] == 'checkstock')
  {
    checkstock($conn, $DATA);
  }
  else if ($DATA['STATUS'] == 'ShowDetailHN_right')
  {
    ShowDetailHN_right($conn, $DATA);
  }
  else if ($DATA['STATUS'] == 'delitem')
  {
    delitem($conn, $DATA);
  }
}
else
{
  $return['status'] = "error";
  $return['msg'] = 'ไม่มีข้อมูลนำเข้า';
  echo json_encode($return);
  mysqli_close($conn);
  die;
}
