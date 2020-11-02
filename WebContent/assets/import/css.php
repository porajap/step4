<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="x-ua-compatible" content="ie=edge">

<title>Demo - Dashboard | Poseintelligence</title>
<link rel="icon" type="image/png" href="../assets/dist/img/p.png">
<!-- Font Awesome Icons -->
<link rel="stylesheet" href="../assets/plugins/fontawesome-free/css/all.min.css">
<!-- overlayScrollbars -->
<link rel="stylesheet" href="../assets/plugins/overlayScrollbars/css/OverlayScrollbars.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="../assets/plugins/icheck-bootstrap/icheck-bootstrap.css">
<link rel="stylesheet" href="../assets/dist/css/adminlte.min.css">
<!-- Google Font: Source Sans Pro -->
<link href="https://fonts.googleapis.com/css2?family=Kanit&display=swap" rel="stylesheet">
<link rel="stylesheet" href="../assets/plugins/daterangepicker/daterangepicker.css">
<!-- DataTables -->
<link rel="stylesheet" href="../assets/datepicker/dist/css/datepicker.min.css">
<link rel="stylesheet" href="../assets/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
<link rel="stylesheet" href="../assets/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
<link rel="stylesheet" href="../assets/dist/css/step.css">
<link rel="stylesheet" href="../assets/plugins/dropify/dist/css/dropify.min.css">
<link rel="stylesheet" href="../assets/datepicker/dist/css/datepicker.min.css">
<link rel="stylesheet" href="../assets/dist/css/sweetalert2.min.css">
<link rel="stylesheet" href="../assets/select2/dist/css/select2.min.css">
<link rel="stylesheet" href="../assets/loadding/jquery.loadingModal.min.css">
<link rel="stylesheet" href="../assets/dropify/dist/css/dropify.min.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<style>

  th{
    position: sticky;
    top: 0;
    z-index: 10;
    height: 10px;
    background-color: #fff !important;
  }
  .subtxt {
    font-size: 30px;
    font-weight: bold;
  }

  .swal2-modal {
    font-family: 'Kanit', sans-serif !important;
  }

  .swal2-confirm,
  .swal2-styled {
    font-family: 'Kanit', sans-serif !important;
  }

  .datepicker {
    z-index: 9999 !important
  }

  body {
    font-family: 'Kanit', sans-serif;
  }

  .btn {
    font-family: 'Kanit', sans-serif;
  }

  .col-center {
    vertical-align: middle !important;
  }

  .nav-item {
    margin-bottom: 20px !important;
  }

  /* .sidebar{
        padding: 0px !important;
    } */
  .card-body-icon {
    position: absolute;
    z-index: 0;
    top: -1.25rem;
    right: -1rem;
    opacity: 0.4;
    font-size: 5rem;
    -webkit-transform: rotate(15deg);
    transform: rotate(15deg);
  }

  th {
    font-size: 16px;
  }

  td {
    font-size: 16px;
  }

  .select2-container--default .select2-selection--single {
    height: 38px;
    border: 1px solid #aaaaaa85;
  }

  #listhn_2:hover {
    background-color: gray;
  }

  .select2-container--default .select2-selection--single .select2-selection__arrow {
    top: 5px;
  }

  .switch {
    position: relative;
    display: inline-block;
    width: 60px;
    height: 34px;
  }

  .switch input {
    opacity: 0;
    width: 0;
    height: 0;
  }

  .slider {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #ccc;
    -webkit-transition: .4s;
    transition: .4s;
  }

  .slider:before {
    position: absolute;
    content: "";
    height: 26px;
    width: 26px;
    left: 4px;
    bottom: 4px;
    background-color: white;
    -webkit-transition: .4s;
    transition: .4s;
  }

  input:checked+.slider {
    background-color: #2196F3;
  }

  input:focus+.slider {
    box-shadow: 0 0 1px #2196F3;
  }

  input:checked+.slider:before {
    -webkit-transform: translateX(26px);
    -ms-transform: translateX(26px);
    transform: translateX(26px);
  }

  /* Rounded sliders */
  .slider.round {
    border-radius: 34px;
  }

  .slider.round:before {
    border-radius: 50%;
  }

  #listhn:hover {
    background-color: #DCD8D7;
    width: 45%;
  }

  #listhn2:hover {
    background-color: #DCD8D7;
  }

  #listhn3:hover {
    background-color: #DCD8D7;
  }

  .square {
    background-color: #5db6ff;
    border: 2px solid #5db6ff;
    border-radius: 25px;
    padding: 15px;
    width: 110%;
    height: auto;
    color: black;
    font-size: 18px;
    font-weight: bold;
  }

  .gasp {
    height: 25px;
  }

  .round2 {
    border: 2px solid red;
    border-radius: 8px;
  }

  .mg4 {
    margin-left: -4%;
  }

</style>