function(doc) {
    if (doc.body)
        emit(null, doc._id)
}