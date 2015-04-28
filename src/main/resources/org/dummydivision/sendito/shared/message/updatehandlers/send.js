function(doc, req) {
    return [{
            _id: req.uuid,
            sender: req.userCtx.name,
            dateSent: new Date(),
            body: req.query.body
        }, req.uuid];
}