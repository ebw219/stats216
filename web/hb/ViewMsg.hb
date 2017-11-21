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
                        <td>should be body here</td>
                    </tr>
                    <tr>
                        <td>{{this.mBody}}</td>
                    </tr>
                    <tr>
                        <td><a href="{{this.mLink}}" type="button" class="btn btn-link" id="ViewMsg-linkload">{{this.mLink}}</a></td>
                    </tr>
                    <tr>
                        <td>{{this.pdf}}</td>
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


