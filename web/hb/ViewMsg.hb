{{!--
<div class="panel panel-default" id="ViewMsg">
     <div class="panel-heading">
     	{{!--  <h3 class="panel-title">{{mData[0].mTitle}}</h3> --}}
         {{!--   <td>there should be one title here</td> --}}
         <h3 class="panel-title">title pls show up</h3>
	</div>
</div>
--}}




<div id="ViewMsg" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Message title should go here</h4>
                <h4 class="modal-title2">{{this.mTitle}}</h4>
            </div>
            <div class="modal-body">
                {{!--<h3 class="panel-title">body here?</h3>--}}
                <table>
                    <tr>
                        <td>should be title and body here</td>
                    </tr>
                    <tr>
                        <td>{{data.mTitle}}</td>
                    </tr>
                    <tr>
                        <td>{{this.mBody}}</td>
                    </tr>
                    <tr>
                        <td>end of info</td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" id="ViewMsg-Close">Close</button>
            </div>
        </div>
    </div>
</div>


