function(doc) {
    if (doc.uri)
        emit(null, doc._id)
}