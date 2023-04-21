package com.demo.PocketStore

class EventData {
    @JvmField
    var id = 0
    @JvmField
    var title: String? = null
    @JvmField
    var description: String? = null
    @JvmField
    var date: String? = null
    @JvmField
    var organisation_id = 0
    @JvmField
    var max_application = 0
    @JvmField
    var current_application = 0
    @JvmField
    var duration: String? = null
    @JvmField
    var location: String? = null
    @JvmField
    var skills_required: String? = null

    constructor() : super() {}
    constructor(
        id: Int, title: String?, description: String?, date: String?,
        organisation_id: Int, max_application: Int, current_application: Int,
        duration: String?, location: String?, skills_required: String?
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.date = date
        this.organisation_id = organisation_id
        this.max_application = max_application
        this.current_application = current_application
        this.duration = duration
        this.location = location
        this.skills_required = skills_required
    }

    constructor(
        title: String?, description: String?, date: String?,
        organisation_id: Int, max_application: Int, current_application: Int,
        duration: String?, location: String?, skills_required: String?
    ) {
        this.title = title
        this.description = description
        this.date = date
        this.organisation_id = organisation_id
        this.max_application = max_application
        this.current_application = current_application
        this.duration = duration
        this.location = location
        this.skills_required = skills_required
    }
}