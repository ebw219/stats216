<div id="NewEntryForm" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Add a New Message</h4>
            </div>
            <div class="modal-body">
                <label for="NewEntryForm-title">Title</label>
                <input class="form-control" type="text" id="NewEntryForm-title" />
                <label for="NewEntryForm-message">Message</label>
                <textarea class="form-control" id="NewEntryForm-message"></textarea>
                <label for="NewEntryForm-linkload">Upload a link (optional)</label>
                <input class="form-control" type="text" id="NewEntryForm-linkload" />
                <label for="NewEntryForm-pdfload">Upload a PDF (optional)</label>
                <input class="file-loading" type="file" id="NewEntryForm-pdfload" />
            <div class="modal-footer">
                <button type="button" class="btn btn-default" id="NewEntryForm-OK">OK</button>
                <button type="button" class="btn btn-default" id="NewEntryForm-Close">Close</button>
            </div>
        </div>
    </div>
</div>