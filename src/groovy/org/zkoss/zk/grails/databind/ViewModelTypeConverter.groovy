package org.zkoss.zk.grails.databind

import org.zkoss.zkplus.databind.TypeConverter
import org.zkoss.zk.ui.Component

@Singleton
class ViewModelTypeConverter implements TypeConverter {

    Object coerceToUi(Object val, Component comp) {
        if(val instanceof Map && val.size() == 2 &&
           val.containsKey("forward") && val.containsKey("reverse")) {
            def c = val['forward']
            return c.call()
        } else if (val instanceof Closure) {
            return val()
        }

        return val
    }

    Object coerceToBean(Object val, Component comp) {
        def context = comp.getAttribute(DataBinder.ZKGRAILS_BINDING_CONTEXT)
        def binder = context['binder']
        def viewModel = context['viewModel']
        def expr = context['expr']
        if(!viewModel.metaClass.hasProperty(viewModel, expr)) {
            return val
        }
        def xval = viewModel."$expr"
        if(xval instanceof Map && xval.size() == 2 && xval.containsKey("forward") && xval.containsKey("reverse")) {
            def c = xval['reverse']
            c.delegate = binder
            c.resolveStrategy = Closure.DELEGATE_FIRST
            c.call(val)
            return TypeConverter.IGNORE
        } else if (xval instanceof Closure) { // it's forward, skip
            return TypeConverter.IGNORE
        }
        return val
    }

}
