<?php
$header="
<style>
table thead tr th,
table tfoot tr th{
    white-space: nowrap;
}
</style>
";

include "layout/header.php";
include "layout/navbar.php";
?>


<div class="container-fluid">

<!-- Page Heading -->
<h1 class="h3 mb-2 text-gray-800">Loại linh kiện</h1>

<!-- DataTales Example -->
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">Danh sách các loại linh kiện</h6>
    </div>
    <div class="card-body">
        <button class="btn btn-primary mb-3" data-toggle="modal" data-target="#modelId">Thêm mới</button>
        <div class="table-responsive">
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                <thead>
                    <tr>
                        <th> </th>
                        <th>Mã loại</th>
                        <th>Tên loại</th>
                        <th style="width:100px;">Chức năng</th>
                    </tr>
                </thead>
                <tbody>
                <?php 
                      foreach( $data['dulieu'] as $item){
                          ?>
                        <tr id="<?php echo $item->code ?>" class="text-center">
                        <td style="max-width:50px;max-height:50px;" ><img style="max-width:100%;max-height:100%" src="./assets/hinhanh/<?php echo $item->img ?>"/></td>
                        <td><?php echo $item->code ?></td>
                        <td><?php echo $item->tenlinhkien ?></td>
                        <td>
                            <ul class="p-0  justify-content-around d-flex" style="list-style:none;">
                                <li class="btnEdit" style="cursor:pointer"><i class="text-success fas fa-edit"></i></li>
                                <li class="btnDel" style="cursor:pointer"><i class="text-danger fas fa-trash-alt"></i></li>
                            </ul>
                        </td>
                    </tr>
                    <?php
                      }
                      ?>
                    
                </tbody>
            </table>
        </div>
    </div>
</div>

</div>
<div class="modal fade" id="modelId" tabindex="-1" role="dialog" aria-labelledby="modelTitleId" aria-hidden="true">
          <div class="modal-dialog" role="document">
              <div class="modal-content">
                  <div class="modal-header">
                      <h5 class="modal-title">Thêm loại linh kiện</h5>
                          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                          </button>
                  </div>
                  <form action="./Home/Insert" enctype="multipart/form-data" method="POST">
                  <div class="modal-body">
                     <div class="form-group">
                       <label for="">Tên loại</label>
                       <input type="text" name="tenloai" id="tenloai" class="form-control" placeholder="" aria-describedby="helpId">
                     </div>
                     <div class="form-group">
                       <label for="">Hình ảnh</label>
                       <input type="file" class="form-control-file" name="hinhanh" id="" placeholder="" aria-describedby="fileHelpId">
                     </div>
                  </div>
                  <div class="modal-footer">
                      <input type="reset" value="hi"/>
                      <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                      <button type="submit"  class="btn btn-primary">Save</button>
                  </div>
                  </form>
              </div>
          </div>
      </div>

                <?php
                $scripts="
                <script>

$('.btnDel').on('click',function(){
  var maloai=$(this).closest('tr').attr('id');
        $.ajax({
          url:'./Del',
          type:'POST',
          data:{
            maloai: maloai
          },
          success: function(data){
            if(data==1){
              alert('xoá thành công');
              window.location.reload();
            }else{
              alert('xoá thất bại');
            }
          }
        })
      })
      </script>
                ";
                include "layout/scripts.php";
                ?>