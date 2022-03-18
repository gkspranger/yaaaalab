```clojure
{
  :text "!help"
  :user "user"
  :source "shell"
  :channel "default"
  :message-responder #'yaaaalab.adapters.shell/reply-to-message
  :message-sender #'yaaaalab.adapters.shell/send-message
  :event-emitter #'yaaaalab.event/emit
  :view-renderer #'yaaaalab.view/render
}
```
